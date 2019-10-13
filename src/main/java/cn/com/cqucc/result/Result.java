package cn.com.cqucc.result;

/**
 * 规定结果类型
 */
public class Result<T> {
    private int code; // 返回码
    private String msg; // 返回信息
    private T data; // 返回数据


    /**
     * 成功的时候调用该方法
     * 因为在Controller中直接new 一个Result对象的话不是很方便 我们直接调用静态方法
     *
     * @param data
     * @param <T>
     * @return
     */
    public static <T> Result<T> success(T data) {
        return new Result<T>(data);
    }

    public static <T> Result<T> error(CodeMsg cm) {
        return new Result<T>(cm);
    }


    /**
     * 生成私有的构造方法 在类中进行初始化
     *
     * @param data
     */
    private Result(T data) {
        this.code = 0;
        this.msg = "success";
        this.data = data;
    }

    private Result(CodeMsg cm) {
        if (cm == null) {
            return;
        }

        this.code = cm.getCode();
        this.msg = cm.getMsg();
    }


    public int getCode() {
        return code;
    }


    public String getMsg() {
        return msg;
    }


    public T getData() {
        return data;
    }
}
