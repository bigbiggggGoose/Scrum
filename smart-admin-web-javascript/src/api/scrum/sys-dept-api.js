/**
 * 部门管理 api 封装
 *
 * @Author:    oyt
 * @Date:      2025-10-11 17:57:33
 * @Copyright  oyt
 */
import { postRequest, getRequest } from '/@/lib/axios';

export const sysDeptApi = {

  /**
   * 分页查询  @author  oyt
   */
  queryPage : (param) => {
    return postRequest('/sysDept/queryPage', param);
  },

  /**
   * 查询全部（用于树）
   */
  listAll: () => {
    return getRequest('/sysDept/listAll');
  },

  /**
   * 增加  @author  oyt
   */
  add: (param) => {
      return postRequest('/sysDept/add', param);
  },

  /**
   * 修改  @author  oyt
   */
  update: (param) => {
      return postRequest('/sysDept/update', param);
  },


  /**
   * 删除  @author  oyt
   */
  delete: (id) => {
      return getRequest(`/sysDept/delete/${id}`);
  },

  /**
   * 批量删除  @author  oyt
   */
  batchDelete: (idList) => {
      return postRequest('/sysDept/batchDelete', idList);
  },

};
