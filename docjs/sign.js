/**
 * @api {get} /signs 获取所有
 * @apiVersion 1.0.0
 * @apiName GetSign
 * @apiGroup Sign
 * @apiDescription 仅供调试。
 * 
 * @apiSuccessExample 成功
 *    HTTP/1.1 200 OK
 *    {
 *      code: "200",
 *      data: [],         // 查询到的所有签到
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
 * @api {post} /signs 创建
 * @apiVersion 1.0.0
 * @apiName PostSign
 * @apiGroup Sign
 * 
 * @apiParam {String} courseId      所属课程id
 * @apiParam {String} startTime     开始时间（格式：2016-06-11 10:00）
 * @apiParam {String} endTime       结束时间（格式：2016-06-11 11:00）
 * @apiParam {String} color         签到提醒颜色
 * @apiParam {String} relatedId     关联指定id的课程的学生
 * 
 * @apiSuccessExample 成功
 *    HTTP/1.1 200 OK
 *    {
 *      code: "200",
 *      data: {},         // 创建后的签到信息
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

/**
 * @api {get} /signs/scanning/:code 扫描签到二维码
 * @apiVersion 1.0.0
 * @apiName GetSignScanning
 * @apiGroup Sign
 * @apiDescription 根据签到码code，返回相关课程信息与最多10条最近的学生签到记录
 * 
 * @apiSuccessExample 成功
 *    HTTP/1.1 200 OK
 *    {
 *      code: "200",
 *      data: 
 *      { 
 *        signId: ''          // 签到id
 *        course: 
 *        {
 *          name: '',
 *          time: '',
 *          location: '',
 *          teacherName: ''
 *        },         
 *        records:            // 最多10条最近的学生签到记录
 *        [{
 *          _id: '',          // 签到记录id
 *          name: '',         // 学生姓名
 *          avatar: ''        // 学生头像url
 *        }]  
 *      },
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