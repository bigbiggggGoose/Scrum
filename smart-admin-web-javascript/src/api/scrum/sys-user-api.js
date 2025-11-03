/**
 * 用户信息表 api 封装
 *
 * @Author:    oyt
 * @Date:      2025-10-11 17:34:30
 * @Copyright  oyt
 */
import { postRequest, getRequest } from '/@/lib/axios';

export const sysUserApi = {

  /**
   * 分页查询  @author  oyt
   */
  queryPage : (param) => {
    return postRequest('/sysUser/queryPage', param);
  },

  /**
   * 增加  @author  oyt
   */
  add: (param) => {
      return postRequest('/sysUser/add', param);
  },

  /**
   * 修改  @author  oyt
   */
  update: (param) => {
      return postRequest('/sysUser/update', param);
  },


  /**
   * 删除  @author  oyt
   */
  delete: (id) => {
      return getRequest(`/sysUser/delete/${id}`);
  },

  /**
   * 批量删除  @author  oyt
   */
  batchDelete: (idList) => {
      return postRequest('/sysUser/batchDelete', idList);
  },

};
