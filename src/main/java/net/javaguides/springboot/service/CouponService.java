package net.javaguides.springboot.service;

import net.javaguides.springboot.exception.ApiException;
import net.javaguides.springboot.model.Coupon;
import net.javaguides.springboot.repository.CouponRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CouponService {

    @Autowired
    private CouponRepository couponRepository;

    public void save(Coupon coupon) {
        if(couponRepository.findByCoupon(coupon.getCoupon()).isPresent()){
            throw new ApiException("coupon already exist", HttpStatus.BAD_REQUEST);
        }
        couponRepository.save(coupon);
    }
    public List<Coupon> findAll() {
       return couponRepository.findAll();
    }
    public void delete(Coupon coupon) {
        couponRepository.delete(coupon);
    }
}
