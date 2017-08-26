package com.jfinal.model;

import com.jfinal.kit.StrKit;
import com.jfinal.plugin.activerecord.*;
import org.apache.commons.lang3.StringUtils;
import tpson.utils.TimeUtil;

import java.math.BigDecimal;
import java.util.*;

public class SqlBuilder<M extends Model<?>> {
	private Table _thisTable;
	private String _tables;
	private String _columns="*";
	private ArrayList<String> _where=new ArrayList<String>();
	private String _group;
	private String _order;
	private String _limit;
	private M _model;
	private ArrayList<Object> _values=new ArrayList<Object>();
	
	public static <T extends Model<T>> SqlBuilder<T> dao(T model){
		SqlBuilder<T> sb = new SqlBuilder<T>(model);
		return sb;
	}
	
	public SqlBuilder(M model){
		_thisTable = TableMapping.me().getTable(model.getClass());
		_model = model;
		_tables = _thisTable.getName();
	}
	
	
	public SqlBuilder<M> table(String table){
		this._tables = table;
		return this;
	} 
	
	public SqlBuilder<M> columns(String ...pair){
		if (pair.length == 1) {
			_columns = pair[0];
		}else{
			_columns = _getJoinStr(pair, "`");
		}
		return this;
	}

	public SqlBuilder<M> columnsExcept(String ...pair){
		Set<String> attrSet = new HashSet<>(_thisTable.getColumnTypeMap().keySet());

		for(String p : pair ){
			attrSet.remove(p);
		}

		_columns = _getJoinStr(attrSet.toArray(new String[attrSet.size()]), "`");

		return this;
	}
	
	private String _getJoinStr(String[] pair, String bothSideStr){
		for(int i = 0; i<pair.length ; i++){
			pair[i] = bothSideStr + pair[i] + bothSideStr;
		}
		return StringUtils.join(pair, ",");
	}
	
	public SqlBuilder<M> limit(int ...pair){
		if (pair.length == 1) {
			_limit = ""+pair[0];
		}else if (pair.length == 2){
			_limit = pair[0] + "," + pair[1];
		}
		return this;
	}
	
	public SqlBuilder<M> where(Object ...pair){
		String whereStr = _getWhere(pair);
		if(!StrKit.isBlank(whereStr))
		_where.add("AND " + whereStr);
		return this;
	}
	
	public SqlBuilder<M> orWhere(Object ...pair){
		String whereStr = _getWhere(pair);
		if(!whereStr.equals(""))
		_where.add("OR " + whereStr);
		return this;
	}

	private boolean checkNotNullValue(Object value){
		return value!=null && !StrKit.isBlank(value.toString());
	}
	
	private String _getWhere(Object ...pair){
		StringBuffer sb = new StringBuffer();
		if (pair.length == 1) {
			sb.append(pair[0]);
		}else if (pair.length == 2){
			if (checkNotNullValue(pair[1])){
				if(pair[1].getClass().isArray() || pair[1].getClass().isInstance(Set.class)){
					sb.append(pair[0] + " in(" + ArrayStrUtil.join(pair[1]) +") ");
				}else{
					sb.append(pair[0] + " = ? ");
					_values.add(pair[1]);
				}
			}
		}else if (pair.length == 3){
			String centerStr = pair[1].toString().toUpperCase();
			if (checkNotNullValue(pair[2])){
				if(centerStr.equals("IN")){
					sb.append(pair[0] + " in(" + ArrayStrUtil.join(pair[2]) +") ");
				}else if(centerStr.equals("LIKE")){
					sb.append(pair[0] + " LIKE ? ");
					_values.add("%" + pair[2] + "%");
				}else{
					sb.append(pair[0] + " "+ pair[1] +" ? ");
					_values.add(pair[2]);
				}
			}

		}
		return sb.toString() ;
	}
	
	public SqlBuilder<M> groupBy(String ...pair){
		if (pair.length == 1) {
			_group = ""+pair[0];
		}else if (pair.length == 2){
			_group = _getJoinStr(pair, "`");
		}
		return this;
	}
	
	public SqlBuilder<M> orderBy(String ...pair){
		if (pair.length == 1) {
			_order = ""+pair[0];
		}else if(pair.length > 1){
			ArrayList<String> list = new ArrayList<String>();
			for(int i = 0; i<pair.length; i++){
				String tag = "ASC";
				if(i<pair.length-1){
					String next = pair[i+1].toString().toUpperCase();
					if(next.equals("ASC") || next.equals("DESC")){
						tag = next;
						list.add("`" + pair[i] + "` " + tag);
						i++;
					}
				}else{
					list.add("`" + pair[i] + "` " + tag);
				}
			}
			_order = _getJoinStr(list.toArray(new String[list.size()]), "");
		}
		return this;
	} 
	
	public String buildBefore(){
		StringBuffer sb = new StringBuffer();
		sb.append("select " + _columns);
		return sb.toString();
	}
	
	public String buildAfter(){
		StringBuffer sb = new StringBuffer();
		sb.append(" FROM " + _tables);
		if(_where.size() > 0){
			sb.append(" WHERE 1 ");
			for(String i : _where){
				sb.append(i);
			}
		}
		
		if(_group != null){
			sb.append(" GROUP BY " + _group);
		}
		
		if(_order != null){
			sb.append(" ORDER BY " + _order);
		}
		
		if(_limit != null){
			sb.append(" LIMIT " + _limit);
		}
		return sb.toString();
	}
	
	public Object[] getValues(){
		return _values.toArray(new Object[_values.size()]);
	}

	@SuppressWarnings("unchecked")
	/**
	 * 进行分页
	 */
	public Page<M> page(int page, int size){
		return (Page<M>) _model.paginate(page, size, buildBefore(), buildAfter(), getValues());
	}

	/**
	 * 进行分页
	 * @param filter
	 * @return
	 */
	public Page<M> page(FormFilter filter){
		return page(filter.getPageNumber(), filter.getPageSize());
	}

	@SuppressWarnings("unchecked")
	/**
	 * 进行分页，并且对列表的数据根据iAutoBind接口 进行外键绑定
	 */
	public Page<M> allPage(int page, int size){
		Page<M> result = (Page<M>)_model.paginate(page, size, buildBefore(), buildAfter(), getValues());
		MergeData.autoBind(result.getList());
		return result;
	}

	/**
	 * 进行分页，补全需要关联的字段
	 * @param filter
	 * @return
	 */
	public Page<M> allPage(FormFilter filter){
		return allPage(filter.getPageNumber(), filter.getPageSize());
	}
	
	@SuppressWarnings("unchecked")
	public List<M> select(){
		return (List<M>) _model.find(buildBefore()+buildAfter(), getValues());
	}

	public List<M> allSelect(){
		List<M> result = select();
		MergeData.autoBind(result);
		return result;
	}

	/**
	 * 获取数据列中的id的Set数组
	 * @return 数据列中的id的Set数组
	 */
	public Set<Long> selectIds(){
		Set<Long> ids = new HashSet<Long>();
		List<? extends Model> result = select();
		result.forEach(model ->{
			ids.add(model.getLong("id"));
		});
		if(ids.size()>0){
			return ids;
		}else{
			return null;
		}
	}

	/**
	 * 获取一个map
	 * @param keyName map中key的name
	 * @param valueName map中value的name
	 * @return 数据列转成的Map
	 */
	public Map<Object,Object> selectMap(String keyName,String valueName){
		Map<Object,Object> map = new HashMap<>();
		List<Record> result = selectRecord();
		result.forEach(record -> {
			map.put(record.get(keyName),record.get(valueName));
		});
		return map;
	}

	public List<Record> selectRecord(){
		return Db.find(buildBefore()+buildAfter(), getValues());
	}
	
	@SuppressWarnings("unchecked")
	public M findFirst(){
		return (M) _model.findFirst(buildBefore() + buildAfter(), getValues());
	}
	
	public int delete(){
		return Db.update("delete " + buildAfter());
	}

	public boolean fakeDelete(String idsStr){
		int[] ids = FormFilter.getIntIds(idsStr);
		return fakeDelete(ids);
	}

	public boolean fakeDelete(int[] ids){
		if(ids == null || ids.length == 0){
			return false;
		}else{
			String idsStr = ArrayStrUtil.join(ids);
			int result;
			if(_thisTable.getColumnTypeMap().containsKey("delete_time")){
				result = Db.update("update "+ _tables +" set is_delete=1,delete_time=? where id in ("+idsStr+")", TimeUtil.getNow());
			}else{
				result = Db.update("update "+ _tables +" set is_delete=1 where id in ("+idsStr+")");
			}
			return result > 0;
		}
	}

	public boolean disable(String idsStr){
		int[] ids = FormFilter.getIntIds(idsStr);
		return disable(ids);
	}

	public boolean disable(int[] ids){
		if(ids == null || ids.length == 0){
			return false;
		}else{
			String idsStr = ArrayStrUtil.join(ids);
			int result;
			if(_thisTable.getColumnTypeMap().containsKey("update_time")){
				result = Db.update("update "+ _tables +" set is_disable=1,update_time=? where id in ("+idsStr+")",TimeUtil.getNow());
			}else{
				result = Db.update("update "+ _tables +" set is_disable=1 where id in ("+idsStr+")");
			}
			return result > 0;
		}
	}
	
	public <T> T queryFirst(){
		return Db.queryFirst(buildBefore() + buildAfter(), getValues());
	}
	
	public long count(){
		_columns = "count(*)";
		return queryFirst();
	}
	
	public <T> T max(String column){
		_columns = "max(`"+ column +"`)";
		return queryFirst();
	}
	
	public <T> T min(String column){
		_columns = "min(`"+ column +"`)";
		return queryFirst();
	}
	
	public <T> T avg(String column){
		_columns = "avg(`"+ column +"`)";
		return queryFirst();
	}

	public long sum(String column){
		_columns = "sum(`"+ column +"`)";
		BigDecimal sum = queryFirst();
		return sum == null ? 0 : sum.longValue();
	}
	
	public String toString(){
		return buildBefore()+buildAfter();
	}
	

}
