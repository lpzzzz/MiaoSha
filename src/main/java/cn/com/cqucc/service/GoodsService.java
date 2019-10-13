package cn.com.cqucc.service;

import cn.com.cqucc.vo.GoodsVo;

import java.util.List;

public interface GoodsService {

    public List<GoodsVo> listGoodsVo();

    public GoodsVo getGoodsVoByGoodsId(long goodsId);

    public boolean reduceStock(GoodsVo goods);

    public void resetStock(List<GoodsVo> goodsList);
}
