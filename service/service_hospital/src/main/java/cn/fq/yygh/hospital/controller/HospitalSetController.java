package cn.fq.yygh.hospital.controller;

import cn.fq.yygh.common.result.ResponseData;
import cn.fq.yygh.hospital.service.HospitalSetService;
import cn.fq.yygh.model.hosp.HospitalSet;
import cn.fq.yygh.vo.hosp.HospitalSetQueryVo;
import cn.hutool.crypto.digest.DigestUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Random;

@Api("医院设置管理")
@RestController
@RequestMapping("/admin/hosp/hospitalSet")
public class HospitalSetController {

    //注入service
    @Autowired
    private HospitalSetService hospitalSetService;

    //1.查询医院设置表中的所有信息
    @ApiOperation("获取列表")
    @GetMapping("/findAll")
    public ResponseData findAll() {
        List<HospitalSet> list = hospitalSetService.list();
        return ResponseData.ok(list);
    }

    //2.删除表中信息
    @ApiOperation("逻辑删除信息")
    @DeleteMapping("/{id}")
    public ResponseData removeHosp(@PathVariable Long id) {
        boolean b = hospitalSetService.removeById(id);
        if (b) {
            return ResponseData.ok();
        } else {
            return ResponseData.fail();
        }
    }

    //3条件查询分页
    @ApiOperation("条件查询分页")
    @PostMapping("/findPage/{current}/{limit}")
    public ResponseData findPageHospSet(@PathVariable long current, @PathVariable Long limit, @RequestBody(required = false) HospitalSetQueryVo hospitalSetQueryVo) {
        //创建Page对象，传递当前页，每页记录数
        Page<HospitalSet> page = new Page<>(current, limit);
        //构建条件
        QueryWrapper<HospitalSet> wrapper = new QueryWrapper<>();
        wrapper.like(StringUtils.isEmpty(hospitalSetQueryVo.getHosname()), "posname", hospitalSetQueryVo.getHosname())
                .eq(StringUtils.isEmpty(hospitalSetQueryVo.getHoscode()), "hoscode", hospitalSetQueryVo.getHoscode());
        //调用方法实现分页查询
        Page<HospitalSet> resPage = hospitalSetService.page(page, wrapper);
        return ResponseData.ok(resPage);
    }

    //4.添加医院设置
    @ApiOperation("添加医院设置")
    @PostMapping("/saveHospitalSet")
    public ResponseData saveHospital(@RequestBody HospitalSet hospitalSet) {
        //设置状态1使用，0不可用
        hospitalSet.setStatus(1);
        //签名密钥
        Random random = new Random();
        //产生的随机数用于md5加密
        String s = DigestUtil.md5Hex(System.currentTimeMillis() + "" + random.nextInt(1000));
        hospitalSet.setSignKey(s);
        boolean save = hospitalSetService.save(hospitalSet);

        return save ? ResponseData.ok() : ResponseData.fail();
    }

    //5.根据id获取医院设置
    @ApiOperation("根据id获取医院设置")
    @GetMapping("/getHospSet/{id}")
    public ResponseData getHospSet(@PathVariable long id) {
        HospitalSet hospitalSet = hospitalSetService.getById(id);
        return ResponseData.ok(hospitalSet);
    }

    //6.修改医院设置
    @ApiOperation("修改医院设置")
    @PostMapping("/updateHospSet")
    public ResponseData updateHospSet(@RequestBody HospitalSet hospitalSet) {
        boolean b = hospitalSetService.updateById(hospitalSet);
        return b ? ResponseData.ok() : ResponseData.fail();
    }

    //7.批量删除医院设置
    @ApiOperation("批量删除医院设置")
    @DeleteMapping("/batchRemove")
    public ResponseData batchRemoveHospSet(@RequestBody List<Long> idList) {
        hospitalSetService.removeByIds(idList);
        return ResponseData.ok();
    }

    //8.医院设置锁定和解锁
    @ApiOperation("医院设置锁定和解锁")
    @PutMapping("/lockHospSet/{id}/{status}")
    public ResponseData lockHospSet(@PathVariable Long id, @PathVariable Integer status) {
        //根据id查询医院设置信息设置信息
        HospitalSet hs = hospitalSetService.getById(id);
        //设置状态
        hs.setStatus(status);
        //调用方法
        hospitalSetService.updateById(hs);
        return ResponseData.ok();
    }

    //9.发送签名密钥
    @ApiOperation("发送签名密钥")
    @PutMapping("/sendKay/{id}")
    public ResponseData lockHospSet(@PathVariable Long id) {
        HospitalSet hs = hospitalSetService.getById(id);
        String signKey = hs.getSignKey();
        String hoscode = hs.getHoscode();
        //TODO 发送短信
        return ResponseData.ok();
    }


}
