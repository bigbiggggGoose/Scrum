/**
 * 网站与邮箱配置表 api 封装
 *
 * @Author:    thr
 * @Date:      2025-10-12 14:40:06
 * @Copyright  wu
 */
import { postRequest, getRequest } from '/@/lib/axios';

export const systemConfigApi = {

  /**
   * 分页查询  @author  thr
   */
  queryPage : (param) => {
    return postRequest('/systemConfig/queryPage', param);
  },

  /**
   * 增加  @author  thr
   */
  add: (param) => {
      return postRequest('/systemConfig/add', param);
  },

  /**
   * 修改  @author  thr
   */
  update: (param) => {
      return postRequest('/systemConfig/update', param);
  },


  /**
   * 删除  @author  thr
   */
  delete: (id) => {
      return getRequest(`/systemConfig/delete/${id}`);
  },

  /**
   * 批量删除  @author  thr
   */
  batchDelete: (idList) => {
      return postRequest('/systemConfig/batchDelete', idList);
  },

};
