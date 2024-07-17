package com.work.workorganization.pojo;


import java.util.HashMap;

public class ResponseBo extends HashMap<String, Object> {
    private static final long serialVersionUid = -8713737118340960775L;

    private static final Integer SUCCESS = 200;
    private static final Integer FAIL = 500;
    private static final Integer NOPERMISSION = 403;
    private static final Integer NOLOGIN = 401;

    public ResponseBo() {
        put("code", SUCCESS);
        put("msg", "操作成功");
        put("data", null);
    }

    public static ResponseBo error(Object msg) {
        ResponseBo responseBo = new ResponseBo();
        responseBo.put("code", FAIL);
        responseBo.put("msg", msg);
        responseBo.put("data", null);
        return responseBo;
    }

    public static ResponseBo error(Object msg, Object data) {
        ResponseBo responseBo = new ResponseBo();
        responseBo.put("code", FAIL);
        responseBo.put("msg", msg);
        responseBo.put("data", data);
        return responseBo;
    }

    public static ResponseBo ok(Object msg, Object data) {
        ResponseBo responseBo = new ResponseBo();
        responseBo.put("code", SUCCESS);
        responseBo.put("msg", msg);
        responseBo.put("data", data);
        return responseBo;
    }

    public static ResponseBo ok(Object data) {
        ResponseBo responseBo = new ResponseBo();
        responseBo.put("code", SUCCESS);
        responseBo.put("msg", "操作成功");
        responseBo.put("data", data);
        return responseBo;
    }

    public static ResponseBo ok() {
        ResponseBo responseBo = new ResponseBo();
        responseBo.put("code", SUCCESS);
        responseBo.put("msg", "操作成功");
        responseBo.put("data", null);
        return responseBo;
    }

    public static ResponseBo noPermission(Object msg) {
        ResponseBo responseBo = new ResponseBo();
        responseBo.put("code", NOPERMISSION);
        responseBo.put("msg", msg);
        responseBo.put("data", null);
        return responseBo;
    }

    public static ResponseBo noLogin(Object msg) {
        ResponseBo responseBo = new ResponseBo();
        responseBo.put("code", NOLOGIN);
        responseBo.put("msg", msg);
        responseBo.put("data", null);
        return responseBo;
    }

    @Override
    public ResponseBo put(String key, Object value) {
        super.put(key, value);
        return this;
    }
}
