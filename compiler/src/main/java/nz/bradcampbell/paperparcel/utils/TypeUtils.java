package nz.bradcampbell.paperparcel.utils;

import com.google.common.base.Objects;
import com.google.common.base.Throwables;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableSet;
import java.lang.reflect.Method;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import javax.lang.model.element.Element;
import javax.lang.model.element.ElementKind;
import javax.lang.model.element.Modifier;
import javax.lang.model.element.PackageElement;
import javax.lang.model.element.TypeElement;
import javax.lang.model.element.TypeParameterElement;
import javax.lang.model.element.VariableElement;
import javax.lang.model.type.ArrayType;
import javax.lang.model.type.DeclaredType;
import javax.lang.model.type.ErrorType;
import javax.lang.model.type.ExecutableType;
import javax.lang.model.type.TypeMirror;
import javax.lang.model.type.TypeVariable;
import javax.lang.model.type.TypeVisitor;
import javax.lang.model.type.WildcardType;
import javax.lang.model.util.SimpleTypeVisitor6;
import javax.lang.model.util.Types;

import static javax.lang.model.element.Modifier.FINAL;
import static javax.lang.model.element.Modifier.PUBLIC;
import static javax.lang.model.element.Modifier.STATIC;
import static javax.lang.model.type.TypeKind.ARRAY;
import static javax.lang.model.type.TypeKind.DECLARED;
import static javax.lang.model.type.TypeKind.EXECUTABLE;
import static javax.lang.model.type.TypeKind.TYPEVAR;

public class TypeUtils {
  private TypeUtils() {
    // No instances.
  }

  public static List<? extends TypeMirror> getArgumentsOfClassFromType(Types types, TypeMirror type,
      Class<?> clazz) {
    if (types.erasure(type).toString().equals(clazz.getName())) {
      DeclaredType declaredType = (DeclaredType) type;
      return declaredType.getTypeArguments();
    }
    List<? extends TypeMirror> superTypes = types.directSupertypes(type);
    List<? extends TypeMirror> result = null;
    for (TypeMirror superType : superTypes) {
      result = getArgumentsOfClassFromType(types, superType, clazz);
      if (result != null) break;
    }
    return result;
  }

  public static boolean hasTypeArguments(TypeElement typeElement) {
    TypeMirror type = typeElement.asType();
    if (type instanceof DeclaredType) {
      DeclaredType declaredType = (DeclaredType) type;
      List<? extends TypeMirror> typeArguments = declaredType.getTypeArguments();
      if (typeArguments.size() > 0) {
        return true;
      }
    }
    return false;
  }

  /** Returns a string for the raw type of {@code type}. Primitive types are always boxed. */
  public static String rawTypeToString(TypeMirror type, char innerClassSeparator) {
    if (!(type instanceof DeclaredType)) {
      throw new IllegalArgumentException("Unexpected type: " + type);
    }
    StringBuilder result = new StringBuilder();
    DeclaredType declaredType = (DeclaredType) type;
    rawTypeToString(result, (TypeElement) declaredType.asElement(), innerClassSeparator);
    return result.toString();
  }

  public static void rawTypeToString(StringBuilder result, TypeElement type,
      char innerClassSeparator) {
    String packageName = getPackage(type).getQualifiedName().toString();
    String qualifiedName = type.getQualifiedName().toString();
    if (packageName.isEmpty()) {
      result.append(qualifiedName.replace('.', innerClassSeparator));
    } else {
      result.append(packageName);
      result.append('.');
      result.append(
          qualifiedName.substring(packageName.length() + 1).replace('.', innerClassSeparator));
    }
  }

  public static PackageElement getPackage(Element type) {
    while (type.getKind() != ElementKind.PACKAGE) {
      type = type.getEnclosingElement();
    }
    return (PackageElement) type;
  }

  /**
   * A singleton is defined by a class with a public static final field named "INSTANCE" with a type
   * assignable from the class itself
   *
   * @param typeUtils Type utils
   * @param el The data class
   * @return true if the class is a singleton, false otherwise
   */
  public static boolean isSingleton(Types typeUtils, TypeElement el) {
    List<? extends Element> enclosedElements = el.getEnclosedElements();
    for (Element e : enclosedElements) {
      Set<Modifier> modifiers = e.getModifiers();
      if (e instanceof VariableElement
          && modifiers.contains(STATIC)
          && modifiers.contains(PUBLIC)
          && modifiers.contains(FINAL)) {
        VariableElement variableElement = (VariableElement) e;
        if (variableElement.getSimpleName().contentEquals("INSTANCE")) {
          return typeUtils.isAssignable(el.asType(), e.asType());
        }
      }
    }
    return false;
  }

  public static boolean isEqualIgnoringWildcards(TypeMirror a, TypeMirror b) {
    return equal(a, b, ImmutableSet.<ComparedElements>of());
  }

  // So EQUAL_IGNORING_WILDCARDS_VISITOR can be a singleton, we maintain visiting state, in particular which types
  // have been seen already, in this object.
  // The logic for handling recursive types like Comparable<T extends Comparable<T>> is very tricky.
  // If we're not careful we'll end up with an infinite recursion. So we record the types that
  // we've already seen during the recursion, and if we see the same pair of types again we just
  // return true provisionally. But "the same pair of types" is itself poorly-defined. We can't
  // just say that it is an equal pair of TypeMirrors, because of course if we knew how to
  // determine that then we wouldn't need the complicated type visitor at all. On the other hand,
  // we can't say that it is an identical pair of TypeMirrors either, because there's no
  // guarantee that the TypeMirrors for the two Ts in Comparable<T extends Comparable<T>> will be
  // represented by the same object, and indeed with the Eclipse compiler they aren't. We could
  // compare the corresponding Elements, since equality is well-defined there, but that's not enough
  // either, because the Element for Set<Object> is the same as the one for Set<String>. So we
  // approximate by comparing the Elements and, if there are any type arguments, requiring them to
  // be identical. This may not be foolproof either but it is sufficient for all the cases we've
  // encountered so far.
  private static final class EqualVisitorParam {
    TypeMirror type;
    Set<ComparedElements> visiting;
  }

  private static class ComparedElements {
    final Element a;
    final ImmutableList<TypeMirror> aArguments;
    final Element b;
    final ImmutableList<TypeMirror> bArguments;

    ComparedElements(
        Element a,
        ImmutableList<TypeMirror> aArguments,
        Element b,
        ImmutableList<TypeMirror> bArguments) {
      this.a = a;
      this.aArguments = aArguments;
      this.b = b;
      this.bArguments = bArguments;
    }
  }

  private static final TypeVisitor<Boolean, EqualVisitorParam> EQUAL_IGNORING_WILDCARDS_VISITOR =
      new SimpleTypeVisitor6<Boolean, EqualVisitorParam>() {
        @Override
        protected Boolean defaultAction(TypeMirror a, EqualVisitorParam p) {
          return a.getKind().equals(p.type.getKind());
        }

        @Override
        public Boolean visitArray(ArrayType a, EqualVisitorParam p) {
          if (p.type.getKind().equals(ARRAY)) {
            ArrayType b = (ArrayType) p.type;
            return equal(a.getComponentType(), b.getComponentType(), p.visiting);
          }
          return false;
        }

        @Override
        public Boolean visitDeclared(DeclaredType a, EqualVisitorParam p) {
          if (p.type.getKind().equals(DECLARED)) {
            DeclaredType b = (DeclaredType) p.type;
            Element aElement = a.asElement();
            Element bElement = b.asElement();
            Set<ComparedElements> newVisiting = visitingSetPlus(
                p.visiting, aElement, a.getTypeArguments(), bElement, b.getTypeArguments());
            //noinspection SimplifiableIfStatement
            if (newVisiting.equals(p.visiting)) {
              // We're already visiting this pair of elements.
              // This can happen for example with Enum in Enum<E extends Enum<E>>. Return a
              // provisional true value since if the Elements are not in fact equal the original
              // visitor of Enum will discover that. We have to check both Elements being compared
              // though to avoid missing the fact that one of the types being compared
              // differs at exactly this point.
              return true;
            }
            return aElement.equals(bElement)
                && equal(a.getEnclosingType(), a.getEnclosingType(), newVisiting)
                && equalLists(a.getTypeArguments(), b.getTypeArguments(), newVisiting);
          }
          return false;
        }

        @Override
        public Boolean visitError(ErrorType a, EqualVisitorParam p) {
          return a.equals(p.type);
        }

        @Override
        public Boolean visitExecutable(ExecutableType a, EqualVisitorParam p) {
          if (p.type.getKind().equals(EXECUTABLE)) {
            ExecutableType b = (ExecutableType) p.type;
            return equalLists(a.getParameterTypes(), b.getParameterTypes(), p.visiting)
                && equal(a.getReturnType(), b.getReturnType(), p.visiting)
                && equalLists(a.getThrownTypes(), b.getThrownTypes(), p.visiting)
                && equalLists(a.getTypeVariables(), b.getTypeVariables(), p.visiting);
          }
          return false;
        }

        @Override
        public Boolean visitTypeVariable(TypeVariable a, EqualVisitorParam p) {
          if (p.type.getKind().equals(TYPEVAR)) {
            TypeVariable b = (TypeVariable) p.type;
            TypeParameterElement aElement = (TypeParameterElement) a.asElement();
            TypeParameterElement bElement = (TypeParameterElement) b.asElement();
            Set<ComparedElements> newVisiting = visitingSetPlus(p.visiting, aElement, bElement);
            if (newVisiting.equals(p.visiting)) {
              // We're already visiting this pair of elements.
              // This can happen with our friend Eclipse when looking at <T extends Comparable<T>>.
              // It incorrectly reports the upper bound of T as T itself.
              return true;
            }
            // We use aElement.getBounds() instead of a.getUpperBound() to avoid having to deal with
            // the different way intersection types (like <T extends Number & Comparable<T>>) are
            // represented before and after Java 8. We do have an issue that this code may consider
            // that <T extends Foo & Bar> is different from <T extends Bar & Foo>, but it's very
            // hard to avoid that, and not likely to be much of a problem in practice.
            return equalLists(aElement.getBounds(), bElement.getBounds(), newVisiting)
                && equal(a.getLowerBound(), b.getLowerBound(), newVisiting)
                && a.asElement().getSimpleName().equals(b.asElement().getSimpleName());
          }
          return false;
        }

        @Override
        public Boolean visitWildcard(WildcardType a, EqualVisitorParam p) {
          // Ignore wildcards. Just get the result of the extends or super bounds.
          if (a.getExtendsBound() != null) {
            return a.getExtendsBound().accept(this, p);
          }
          if (a.getSuperBound() != null) {
            return a.getSuperBound().accept(this, p);
          }
          return false;
        }

        @Override
        public Boolean visitUnknown(TypeMirror a, EqualVisitorParam p) {
          throw new UnsupportedOperationException();
        }

        private Set<ComparedElements> visitingSetPlus(
            Set<ComparedElements> visiting, Element a, Element b) {
          ImmutableList<TypeMirror> noArguments = ImmutableList.of();
          return visitingSetPlus(visiting, a, noArguments, b, noArguments);
        }

        private Set<ComparedElements> visitingSetPlus(
            Set<ComparedElements> visiting,
            Element a, List<? extends TypeMirror> aArguments,
            Element b, List<? extends TypeMirror> bArguments) {
          ComparedElements comparedElements =
              new ComparedElements(
                  a, ImmutableList.copyOf(aArguments),
                  b, ImmutableList.copyOf(bArguments));
          Set<ComparedElements> newVisiting = new HashSet<>(visiting);
          newVisiting.add(comparedElements);
          return newVisiting;
        }
      };

  private static final Class<?> INTERSECTION_TYPE;
  private static final Method GET_BOUNDS;
  static {
    Class<?> c;
    Method m;
    try {
      c = Class.forName("javax.lang.model.type.IntersectionType");
      m = c.getMethod("getBounds");
    } catch (Exception e) {
      c = null;
      m = null;
    }
    INTERSECTION_TYPE = c;
    GET_BOUNDS = m;
  }

  private static boolean equal(TypeMirror a, TypeMirror b, Set<ComparedElements> visiting) {
    // TypeMirror.equals is not guaranteed to return true for types that are equal, but we can
    // assume that if it does return true then the types are equal. This check also avoids getting
    // stuck in infinite recursion when Eclipse decrees that the upper bound of the second K in
    // <K extends Comparable<K>> is a distinct but equal K.
    // The javac implementation of ExecutableType, at least in some versions, does not take thrown
    // exceptions into account in its equals implementation, so avoid this optimization for
    // ExecutableType.
    if (Objects.equal(a, b) && !(a instanceof ExecutableType)) {
      return true;
    }
    EqualVisitorParam p = new EqualVisitorParam();
    p.type = b;
    p.visiting = visiting;
    if (INTERSECTION_TYPE != null) {
      if (isIntersectionType(a)) {
        return equalIntersectionTypes(a, b, visiting);
      } else if (isIntersectionType(b)) {
        return false;
      }
    }
    return (a == b) || (a != null && b != null && a.accept(EQUAL_IGNORING_WILDCARDS_VISITOR, p));
  }

  private static boolean isIntersectionType(TypeMirror t) {
    return t != null && t.getKind().name().equals("INTERSECTION");
  }

  // The representation of an intersection type, as in <T extends Number & Comparable<T>>, changed
  // between Java 7 and Java 8. In Java 7 it was modeled as a fake DeclaredType, and our logic
  // for DeclaredType does the right thing. In Java 8 it is modeled as a new type IntersectionType.
  // In order for our code to run on Java 7 (and Java 6) we can't even mention IntersectionType,
  // so we can't override visitIntersectionType(IntersectionType). Instead, we discover through
  // reflection whether IntersectionType exists, and if it does we extract the bounds of the
  // intersection ((Number, Comparable<T>) in the example) and compare them directly.
  @SuppressWarnings("unchecked")
  private static boolean equalIntersectionTypes(
      TypeMirror a, TypeMirror b, Set<ComparedElements> visiting) {
    if (!isIntersectionType(b)) {
      return false;
    }
    List<? extends TypeMirror> aBounds;
    List<? extends TypeMirror> bBounds;
    try {
      aBounds = (List<? extends TypeMirror>) GET_BOUNDS.invoke(a);
      bBounds = (List<? extends TypeMirror>) GET_BOUNDS.invoke(b);
    } catch (Exception e) {
      throw Throwables.propagate(e);
    }
    return equalLists(aBounds, bBounds, visiting);
  }

  private static boolean equalLists(
      List<? extends TypeMirror> a, List<? extends TypeMirror> b,
      Set<ComparedElements> visiting) {
    int size = a.size();
    if (size != b.size()) {
      return false;
    }
    // Use iterators in case the Lists aren't RandomAccess
    Iterator<? extends TypeMirror> aIterator = a.iterator();
    Iterator<? extends TypeMirror> bIterator = b.iterator();
    while (aIterator.hasNext()) {
      if (!bIterator.hasNext()) {
        return false;
      }
      TypeMirror nextMirrorA = aIterator.next();
      TypeMirror nextMirrorB = bIterator.next();
      if (!equal(nextMirrorA, nextMirrorB, visiting)) {
        return false;
      }
    }
    return !aIterator.hasNext();
  }
}
