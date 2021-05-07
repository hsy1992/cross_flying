package cn.net.hylink.flying.util

import android.os.Parcelable
import java.io.Serializable
import java.lang.reflect.*
import java.lang.reflect.Array
import java.util.*

/**
 * @ClassName ClassUtil
 * @Description class 工具
 * @Author haosiyuan
 * @Date 2021/4/21 18:59
 * @Version 1.0
 */
object ClassUtil {

    val EMPTY_TYPE_ARRAY = arrayOfNulls<Type>(1)

    fun methodError(method: Method, message: String, vararg arg: Any?): RuntimeException =
        methodError(
            method,
            null,
            message,
            arg
        )

    fun methodError(method: Method, cause: Throwable?, message: String,
                    vararg args: Any?): RuntimeException {
        return IllegalArgumentException("""
            ${String.format(message, *args)} for method ${method.declaringClass.simpleName}.${method.name}
            """, cause)
    }

    fun parameterError(method: Method?, cause: Throwable?,
                       p: Int, message: String, vararg args: Any?): RuntimeException {
        return methodError(
            method!!,
            cause,
            message + " (parameter #" + (p + 1) + ")",
            *args
        )
    }

    fun parameterError(method: Method?, p: Int, message: String, vararg args: Any?): RuntimeException {
        return methodError(
            method!!,
            message + " (parameter #" + (p + 1) + ")",
            *args
        )
    }

    fun getRawType(type: Type): Class<*> {
        if (type is Class<*>) {
            return type
        }
        if (type is ParameterizedType) {
            val rawType = type.rawType
            if (rawType !is Class<*>) throw IllegalArgumentException()
            return rawType
        }
        if (type is GenericArrayType) {
            val componentType = type.genericComponentType
            return Array.newInstance(
                getRawType(
                    componentType
                ), 0).javaClass
        }
        if (type is TypeVariable<*>) {
            return Any::class.java
        }
        if (type is WildcardType) {
            return getRawType(type.upperBounds[0])
        }

        throw IllegalArgumentException("Expected a Class, ParameterizedType, or "
                + "GenericArrayType, but <" + type + "> is of type " + type.javaClass.name)
    }

    /**
     * 判断是否相同
     */
    fun equals(a: Type, b: Type): Boolean {
        return when {
            a === b -> {
                true
            }
            a is Class<*> -> {
                a == b
            }
            a is ParameterizedType -> {
                if (b !is ParameterizedType) return false
                val pa = a as ParameterizedType
                val pb = b as ParameterizedType
                val ownerA: Any? = pa.ownerType
                val ownerB: Any = pb.ownerType
                ((ownerA === ownerB || ownerA != null && ownerA == ownerB)
                        && pa.rawType == pb.rawType && Arrays.equals(pa.actualTypeArguments, pb.actualTypeArguments))
            }
            a is GenericArrayType -> {
                if (b !is GenericArrayType) return false
                val ga = a as GenericArrayType
                val gb = b as GenericArrayType
                equals(
                    ga.genericComponentType,
                    gb.genericComponentType
                )
            }
            a is WildcardType -> {
                if (b !is WildcardType) return false
                val wa = a as WildcardType
                val wb = b as WildcardType
                (Arrays.equals(wa.upperBounds, wb.upperBounds)
                        && Arrays.equals(wa.lowerBounds, wb.lowerBounds))
            }
            a is TypeVariable<*> -> {
                if (b !is TypeVariable<*>) return false
                val va = a as TypeVariable<*>
                val vb = b as TypeVariable<*>
                (va.genericDeclaration === vb.genericDeclaration
                        && va.name == vb.name)
            }
            else -> {
                false
            }
        }
    }

    /**
     * 返回超型
     * @param rawType 原始类型
     */
    private fun getGenericSupertype(context: Type?, rawType: Class<*>, toResolve: Class<*>): Type? {
        var rawType = rawType
        if (toResolve == rawType) return context

        // toResolve 是否是接口
        if (toResolve.isInterface) {
            val interfaces = rawType.interfaces
            for (i in interfaces.indices) {
                if (interfaces[i] == toResolve) {
                    return rawType.genericInterfaces[i]
                } else if (toResolve.isAssignableFrom(interfaces[i])) {
                    return getGenericSupertype(
                        rawType.genericInterfaces[i],
                        interfaces[i],
                        toResolve
                    )
                }
            }
        }

        //检查类型
        if (!rawType.isInterface) {
            while (rawType != Any::class.java) {
                val rawSupertype = rawType.superclass
                if (rawSupertype == toResolve) {
                    return rawType.genericSuperclass
                } else if (toResolve.isAssignableFrom(rawSupertype)) {
                    return getGenericSupertype(
                        rawType.genericSuperclass,
                        rawSupertype,
                        toResolve
                    )
                }
                rawType = rawSupertype
            }
        }

        return toResolve
    }

    private fun indexOf(array: kotlin.Array<Any>, toFind: Any): Int {
        for (i in array.indices) {
            if (toFind == array[i]) return i
        }
        throw NoSuchElementException()
    }

    private fun typeToString(type: Type): String {
        return if (type is Class<*>) type.name else type.toString()
    }

    /**
     * 获取超类类型
     */
    private fun getSupertype(context: Type?, contextRawType: Class<*>, supertype: Class<*>): Type? {
        require(supertype.isAssignableFrom(contextRawType))
        return resolve(
            context, contextRawType,
            getGenericSupertype(
                context,
                contextRawType,
                supertype
            )
        )
    }

    private fun resolve(context: Type?, contextRawType: Class<*>?, toResolve: Type?): Type? {
        var toResolve = toResolve
        while (true) {
            if (toResolve is TypeVariable<*>) {
                toResolve =
                    resolveTypeVariable(
                        context,
                        contextRawType,
                        toResolve
                    )
                if (toResolve == toResolve) {
                    return toResolve
                }
            } else if (toResolve is Class<*> && toResolve.isArray) {
                val original = toResolve
                val componentType: Type = original.componentType
                val newComponentType =
                    resolve(
                        context,
                        contextRawType,
                        componentType
                    )
                return if (componentType == newComponentType) original else newComponentType?.let {
                    GenericArrayTypeImpl(it)
                }
            } else if (toResolve is GenericArrayType) {
                val original = toResolve
                val componentType = original.genericComponentType
                val newComponentType =
                    resolve(
                        context,
                        contextRawType,
                        componentType
                    )
                return if (componentType == newComponentType) original else newComponentType?.let {
                    GenericArrayTypeImpl(it)
                }
            } else if (toResolve is ParameterizedType) {
                val original = toResolve
                val ownerType = original.ownerType
                val newOwnerType = resolve(
                    context,
                    contextRawType,
                    ownerType
                )
                var changed = newOwnerType != ownerType
                var args = original.actualTypeArguments
                for (i in args.indices) {
                    val resolvedTypeArgument =
                        resolve(
                            context,
                            contextRawType,
                            args[i]
                        )
                    if (resolvedTypeArgument != args[i]) {
                        if (!changed) {
                            args = args.clone()
                            changed = true
                        }
                        args[i] = resolvedTypeArgument
                    }
                }
                return if (changed) newOwnerType?.let {
                    ParameterizedTypeImpl(
                        it,
                        original.rawType,
                        *args
                    )
                } else original
            } else if (toResolve is WildcardType) {
                val original = toResolve
                val originalLowerBound = original.lowerBounds
                val originalUpperBound = original.upperBounds
                if (originalLowerBound.size == 1) {
                    val lowerBound = resolve(
                        context,
                        contextRawType,
                        originalLowerBound[0]
                    )
                    if (lowerBound !== originalLowerBound[0]) {
                        val upperBoundsType = arrayOfNulls<Type>(1)
                        upperBoundsType[0] = Any::class.java
                        return WildcardTypeImpl(
                            upperBoundsType,
                            arrayOf(lowerBound)
                        )
                    }
                } else if (originalUpperBound.size == 1) {
                    val upperBound = resolve(
                        context,
                        contextRawType,
                        originalUpperBound[0]
                    )
                    if (upperBound !== originalUpperBound[0]) {
                        return WildcardTypeImpl(
                            arrayOf(upperBound),
                            EMPTY_TYPE_ARRAY
                        )
                    }
                }
                return original
            } else {
                return toResolve
            }
        }
    }

    /**
     * 解析类型变量
     */
    private fun resolveTypeVariable(
            context: Type?, contextRawType: Class<*>?, unknown: TypeVariable<*>): Type? {
        val declaredByRaw: Class<*> = declaringClassOf(
            unknown
        ) ?: return unknown

        val declaredBy = contextRawType?.let {
            getGenericSupertype(
                context,
                contextRawType,
                declaredByRaw
            )
        }
        if (declaredBy is ParameterizedType) {
            val index = indexOf(
                arrayOf(declaredByRaw.typeParameters),
                unknown
            )
            return declaredBy.actualTypeArguments[index]
        }
        return unknown
    }

    /**
     * 返回 typeVariable 的声明类
     */
    private fun declaringClassOf(typeVariable: TypeVariable<*>): Class<*>? {
        val genericDeclaration = typeVariable.genericDeclaration
        return if (genericDeclaration is Class<*>) genericDeclaration else null
    }

    /**
     * 是不是基本类型
     */
    private fun checkNotPrimitive(type: Type?) {
        require(!(type is Class<*> && type.isPrimitive))
    }

    /**
     * 注解是否存在
     */
    fun isAnnotationPresent(annotations: kotlin.Array<Annotation?>,
                            cls: Class<out Annotation?>): Boolean {
        for (annotation in annotations) {
            if (cls.isInstance(annotation)) {
                return true
            }
        }
        return false
    }

    /**
     * 获取泛型变量的上边界
     */
    fun getParameterUpperBound(index: Int, type: ParameterizedType): Type? {
        val types = type.actualTypeArguments
        require(!(index < 0 || index >= types.size)) { "Index " + index + " not in range [0," + types.size + ") for " + type }
        val paramType = types[index]
        return if (paramType is WildcardType) {
            paramType.upperBounds[0]
        } else paramType
    }

    /**
     * 获取泛型变量的下边界
     */
    private fun getParameterLowerBound(index: Int, type: ParameterizedType): Type? {
        val paramType = type.actualTypeArguments[index]
        return if (paramType is WildcardType) {
            paramType.lowerBounds[0]
        } else paramType
    }

    /**
     * 是否有无法解析的类型
     */
    private fun hasUnresolvableType(type: Type?): Boolean {
        if (type is Class<*>) {
            return false
        }
        if (type is ParameterizedType) {
            for (typeArgument in type.actualTypeArguments) {
                if (hasUnresolvableType(
                        typeArgument
                    )
                ) {
                    return true
                }
            }
            return false
        }
        if (type is GenericArrayType) {
            return hasUnresolvableType(type.genericComponentType)
        }
        if (type is TypeVariable<*>) {
            return true
        }
        if (type is WildcardType) {
            return true
        }
        val className = if (type == null) "null" else type.javaClass.name
        throw IllegalArgumentException("Expected a Class, ParameterizedType, or "
                + "GenericArrayType, but <" + type + "> is of type " + className)
    }

    /**
     * 获取有效接口
     */
    fun getValidInterface(clazz: Class<*>): kotlin.Array<Class<*>>? {
        val interfaces = clazz.interfaces
        val interfaceArray = ArrayList<Class<*>>(interfaces.size)
        for (i in interfaces.indices) {
            val anInterface = interfaces[i]
            if (Parcelable::class.java.isAssignableFrom(anInterface)
                    || Cloneable::class.java.isAssignableFrom(anInterface)
                    || Serializable::class.java.isAssignableFrom(anInterface)) {
                continue
            }
            interfaceArray.add(anInterface)
        }
        return interfaceArray.toArray(arrayOf())
    }

    class ParameterizedTypeImpl constructor(
            private val ownerType: Type,
            private val rawType: Type,
            vararg typeArguments: Type) : ParameterizedType {

        private var typeArgumentsClone: kotlin.Array<out Type> = typeArguments.clone()

        init {
            require(!(rawType is Class<*> && (rawType.enclosingClass == null)))
            for (typeArgument in typeArguments) {
                checkNotPrimitive(
                    typeArgument
                )
            }
        }

        override fun getRawType(): Type = rawType

        override fun getOwnerType(): Type = ownerType

        override fun getActualTypeArguments(): kotlin.Array<out Type> = typeArgumentsClone.clone()

        override fun equals(other: Any?): Boolean {
            return other is ParameterizedType && equals(
                this,
                (other as ParameterizedType?)!!
            )
        }

        override fun hashCode(): Int {
            return (typeArgumentsClone.contentHashCode()
                    xor rawType.hashCode()
                    xor ownerType.hashCode())
        }

        override fun toString(): String {
            if (typeArgumentsClone.isEmpty()) return typeToString(
                rawType
            )
            val result = StringBuilder(30 * (typeArgumentsClone.size + 1))
            result.append(
                typeToString(
                    rawType
                )
            )
            result.append("<").append(
                typeToString(
                    typeArgumentsClone[0]
                )
            )
            for (i in 1 until typeArgumentsClone.size) {
                result.append(", ").append(
                    typeToString(
                        typeArgumentsClone[i]
                    )
                )
            }
            return result.append(">").toString()
        }
    }

    private class GenericArrayTypeImpl constructor(
            private val componentType: Type) : GenericArrayType {

        override fun getGenericComponentType(): Type = componentType

        override fun equals(other: Any?): Boolean {
            return other is GenericArrayType && equals(
                this,
                other
            )
        }

        override fun hashCode(): Int {
            return componentType.hashCode()
        }

        override fun toString(): String {
            return typeToString(componentType) + "[]"
        }
    }

    private class WildcardTypeImpl constructor(
            upperBounds: kotlin.Array<Type?>,
            lowerBounds: kotlin.Array<Type?>) : WildcardType {

        private var upperBound: Type? = null
        private var lowerBound: Type? = null

        init {
            require(lowerBounds.size <= 1)
            require(upperBounds.size == 1)
            if (lowerBounds.size == 1) {
                checkNotPrimitive(lowerBounds[0])
                require(upperBounds[0] !== Any::class.java)
                lowerBound = lowerBounds[0]
                upperBound = Any::class.java
            } else {
                checkNotPrimitive(upperBounds[0])
                lowerBound = null
                upperBound = upperBounds[0]
            }
        }

        override fun getLowerBounds(): kotlin.Array<Type?> = arrayOf(upperBound)

        override fun getUpperBounds(): kotlin.Array<Type?> =
                if (lowerBound != null) arrayOf(lowerBound) else EMPTY_TYPE_ARRAY

        override fun equals(other: Any?): Boolean {
            return other is WildcardType && equals(
                this,
                other
            )
        }

        override fun hashCode(): Int =
                (if (lowerBound != null) 31 + lowerBound.hashCode() else 1) xor 31 + upperBound.hashCode()

        override fun toString(): String {
            if (lowerBound != null) return "? super " + typeToString(
                lowerBound!!
            )
            return if (upperBound === Any::class.java) "?" else "? extends " + typeToString(
                upperBound!!
            )
        }
    }

    fun throwIfFatal(t: Throwable?) {
        when (t) {
            is VirtualMachineError -> {
                throw (t as VirtualMachineError?)!!
            }
            is ThreadDeath -> {
                throw (t as ThreadDeath?)!!
            }
            is LinkageError -> {
                throw (t as LinkageError?)!!
            }
        }
    }
}