package cn.net.hylink.flying.constant

/**
 * @ClassName Constant
 * @Description 常量池
 * @Author haosiyuan
 * @Date 2021/4/20 16:44
 * @Version 1.0
 */
object Constant {

    /**
     * 前缀
     */
    const val PREFIX = "Flying-"

    /**
     * Router key
     */
    const val FLY_KEY_ROUTER = "key_path"

    /**
     * 参数flag
     */
    const val FLY_KEY_FLAGS = "key_flags"

    /**
     * 返回code
     */
    const val FLY_KEY_RESPONSE_CODE = "response_code"

    const val FLY_KEY_RESPONSE = "key_response"

    /**
     * 类型
     */
    const val FLY_KEY_TYPE = "type"

    /**
     * 参数 长度
     */
    const val FLY_KEY_LENGTH = "key_length"

    const val FLY_KEY_INDEX = "key_%d"

    const val FLY_KEY_CLASS_INDEX = "key_class_%d"

    object Parameters {

        const val FLAGS_BUNDLE = 1
    }

    object ResponseCode {
        const val FLY_RESPONSE_RESULT_NO_SUCH_METHOD = 404
        const val FLY_RESPONSE_RESULT_LOST_CLASS = 405
        const val FLY_RESPONSE_RESULT_ILLEGAL_ACCESS = 403
        const val FLY_RESPONSE_RESULT_NOT_FOUND_ROUTE = 402
        const val FLY_RESPONSE_RESULT_REMOTE_EXCEPTION = 500
        const val FLY_RESPONSE_RESULT_SUCCESS = 200
    }

    object Type {
        const val FLY_TYPE_BUNDLE = 1;
    }
}