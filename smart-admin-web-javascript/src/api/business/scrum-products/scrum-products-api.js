/**
 * 产品表 api 封装
 *
 * @Author:    cmz
 * @Date:      2025-10-11 22:39:41
 * @Copyright  1
 */
import { postRequest, getRequest } from '/@/lib/axios';

export const scrumProductsApi = {

  /**
   * 分页查询  @author  cmz
   */
  queryPage : (param) => {
    return postRequest('/scrumProducts/queryPage', param);
  },

  /**
   * 增加  @author  cmz
   */
  add: (param) => {
      return postRequest('/scrumProducts/add', param);
  },

  /**
   * 修改  @author  cmz
   */
  update: (param) => {
      return postRequest('/scrumProducts/update', param);
  },


  /**
   * 删除  @author  cmz
   */
  delete: (id) => {
      return getRequest(`/scrumProducts/delete/${id}`);
  },

  /**
   * 批量删除  @author  cmz
   */
  batchDelete: (idList) => {
      return postRequest('/scrumProducts/batchDelete', idList);
  },

};
