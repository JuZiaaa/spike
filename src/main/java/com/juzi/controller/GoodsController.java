package com.juzi.controller;

import com.juzi.domain.User;
import com.juzi.redis.GoodsKey;
import com.juzi.redis.KeyPrefix;
import com.juzi.redis.RedisService;
import com.juzi.result.Result;
import com.juzi.service.GoodsService;
import com.juzi.vo.GoodsDetailVo;
import com.juzi.vo.GoodsVo;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.thymeleaf.context.WebContext;
import org.thymeleaf.spring5.view.ThymeleafViewResolver;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.OutputStream;
import java.util.List;

/**
 * @author: juzi
 * @date: 2019/4/8
 * @time: 0:25
 */
@RequestMapping("goods")
@Controller
public class GoodsController{
        /** 加一个配置项 */
        @Value("#{'${pageCache.enbale}'}")
        private boolean pageCacheEnable;
        @Autowired
        GoodsService goodsService;
        @Autowired
        ThymeleafViewResolver thymeleafViewResolver;
        @Autowired
        RedisService redisService;

        @RequestMapping(value="/to_list")
        public String list(HttpServletRequest request, HttpServletResponse response, Model model, User user) {
            model.addAttribute("user", user);
            List<GoodsVo> goodsList = goodsService.listGoodsVo();
            model.addAttribute("goodsList", goodsList);
            return render(request, response, model, "goods_list", GoodsKey.getGoodsList, "");
        }

        public String render(HttpServletRequest request, HttpServletResponse response, Model model, String tplName, KeyPrefix prefix, String key) {
            if(!pageCacheEnable) {
                return tplName;

            }
            //取缓存
            String html = redisService.get(prefix, key, String.class);
            if(!StringUtils.isEmpty(html)) {
                out(response, html);
                return null;
            }
            //手动渲染
            WebContext ctx = new WebContext(request,response,
                    request.getServletContext(),request.getLocale(), model.asMap());
            html = thymeleafViewResolver.getTemplateEngine().process(tplName, ctx);
            if(!StringUtils.isEmpty(html)) {
                redisService.set(prefix, key, html);
            }
            out(response, html);
            return null;
        }

        public static void out(HttpServletResponse res, String html){
            res.setContentType("text/html");
            res.setCharacterEncoding("UTF-8");
            try{
                OutputStream out = res.getOutputStream();
                out.write(html.getBytes("UTF-8"));
                out.flush();
                out.close();
            }catch(Exception e){
                e.printStackTrace();
            }

        }


    @RequestMapping(value="/detail/{goodsId}")
    @ResponseBody
    public Result<GoodsDetailVo> detail(HttpServletRequest request, HttpServletResponse response, Model model,User user,
                                        @PathVariable("goodsId")long goodsId) {
        GoodsVo goods = goodsService.getGoodsVoByGoodsId(goodsId);
        long startAt = goods.getStartDate().getTime();
        long endAt = goods.getEndDate().getTime();
        long now = System.currentTimeMillis();
        int spikeStatus = 0;
        int remainSeconds = 0;
        if(now < startAt ) {//秒杀还没开始，倒计时
            spikeStatus = 0;
            remainSeconds = (int)((startAt - now )/1000);
        }else  if(now > endAt){//秒杀已经结束
            spikeStatus = 2;
            remainSeconds = -1;
        }else {//秒杀进行中
            spikeStatus = 1;
            remainSeconds = 0;
        }
        GoodsDetailVo vo = new GoodsDetailVo();
        vo.setGoods(goods);
        vo.setUser(user);
        vo.setRemainSeconds(remainSeconds);
        vo.setSpikeStatus(spikeStatus);
        return Result.success(vo);
    }



}
