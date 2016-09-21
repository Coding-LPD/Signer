/**
 * @api {get} /publickey 获取公钥
 * @apiVersion 1.0.0
 * @apiName GetPublickey
 * @apiGroup Key
 *
 * @apiSuccessExample 成功：
 *     HTTP/1.1 200 OK
 *     {
 *        code: "200",
 *        data: "pem格式的公钥",
 *        msg:  "请求成功"
 *     }
 * 
 * @apiErrorExample 失败：
 *    HTTP/1.1 200 OK
 *    {
 *      code: "XXXX",
 *      data: "",
 *      msg:  "错误信息"
 *    }
 */