package net.javaguides.springboot.controller;

import net.javaguides.springboot.model.Coupon;
import net.javaguides.springboot.service.CouponService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/coupon")
public class CouponController {
    @Autowired
    private CouponService couponService;
    @PostMapping
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<String> create(@RequestBody Coupon coupon){
        couponService.save(coupon);
        return ResponseEntity.ok("Coupon Added Successfully");
    }

    @GetMapping
    public ResponseEntity<List<Coupon>> getAllCoupon(){
        return ResponseEntity.ok(couponService.findAll());
    }
    @DeleteMapping
    public ResponseEntity<String> delete(@RequestBody Coupon coupon){
        couponService.delete(coupon);
        return ResponseEntity.ok("Coupon Deleted Successfully");
    }
}
