/**
 * @api {get} /signRecords 获取所有
 * @apiVersion 1.0.0
 * @apiName GetSignRecord
 * @apiGroup SignRecord
 * @apiDescription 仅供调试。
 * 
 * @apiSuccessExample 成功
 *    HTTP/1.1 200 OK
 *    {
 *      code: "200",
 *      data: [],         // 查询到的所有签到记录
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
 * @api {post} /signRecords 学生进行签到
 * @apiVersion 1.0.0
 * @apiName PostSignRecord
 * @apiGroup SignRecord
 * 
 * @apiParam {String} signId        所属签到id
 * @apiParam {String} phoneId       手机唯一标识
 * @apiParam {String} studentId     学生id
 * @apiParam {Number} type          签到类型（0：课前，1：课后）
 * @apiParam {Number} battery       手机电量
 * @apiParam {Number} longitude     精度
 * @apiParam {Number} latitude      纬度
 * 
 * @apiSuccessExample 成功
 *    HTTP/1.1 200 OK
 *    {
 *      code: "200",
 *      data: {},         // 创建后的签到记录
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
 * @api {post} /signRecords/search 查询
 * @apiVersion 1.0.0
 * @apiName PostSignRecordSearch
 * @apiGroup SignRecord
 * @apiDescription 根据指定字段名和值查询签到记录。（必须要有查询条件）
 * 
 * @apiParam {String} _id             签到记录id
 * @apiParam {String} signId          签到id
 * @apiParam {String} phoneId         手机唯一标识
 * @apiParam {String} studentId       学生id
 * @apiParam {String} studentName     学生姓名
 * @apiParam {String} studentAvatar   学生头像url
 * @apiParam {Number} distance        签到距离
 * @apiParam {Number} state           签到批准状态（0：未处理 1：已准许 2：已拒绝）
 * @apiParam {Number} type            签到类型（0：课前，1：课后）
 * @apiParam {String} createdAt       签到时间
 * @apiParam {Number} battery         手机电量
 * 
 * @apiSuccessExample 成功：
 *    HTTP/1.1 200 OK
 *    {
 *      code: "200",
 *      data: [],           // 查询到的所有签到记录
 *      msg:  "操作成功"
 *    } 
 * 
 * @apiErrorExample 失败：
 *    HTTP/1.1 200 OK
 *    {
 *      code: "XXXX",
 *      data: [],           // 空数组
 *      msg:  "错误信息"
 *    }
 */