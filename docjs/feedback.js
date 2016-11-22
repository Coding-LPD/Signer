/**
 * @api {get} /feedbacks 获取所有
 * @apiVersion 1.0.0
 * @apiName GetFeedback
 * @apiGroup Feedback
 * @apiDescription 仅供调试。
 * 
 * @apiSuccessExample 成功
 *    HTTP/1.1 200 OK
 *    {
 *      code: "200",
 *      data: [],         // 查询到的所有反馈
 *      msg:  "操作成功"
 *    }
 * 
 * @apiErrorExample 失败：
 *    HTTP/1.1 200 OK
 *    {
 *      code: "XXXX",
 *      data: [],         // 空数组
 *      msg:  "错误信息"
 *    }
 */

/**
 * @api {post} /feedbacks 创建
 * @apiVersion 1.0.0
 * @apiName PostFeedback
 * @apiGroup Feedback
 * 
 * @apiParam {String} studentId     反馈者为学生，则发送该字段
 * @apiParam {String} teacherId     反馈者为教师，则发送该字段
 * @apiParam {String} name          反馈者姓名
 * @apiParam {String} phone         反馈者联系电话
 * @apiParam {String} content       反馈内容
 * 
 * @apiSuccessExample 成功
 *    HTTP/1.1 200 OK
 *    {
 *      code: "200",
 *      data: {},         // 创建后的反馈信息
 *      msg:  "操作成功"
 *    }
 * 
 * @apiErrorExample 失败：
 *    HTTP/1.1 200 OK
 *    {
 *      code: "XXXX",
 *      data: {},         // 空对象
 *      msg:  "错误信息"
 *    }
 */