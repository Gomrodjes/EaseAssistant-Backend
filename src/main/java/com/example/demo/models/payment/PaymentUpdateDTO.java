package com.example.demo.models.payment;

import com.example.demo.enums.StatePayment;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class PaymentUpdateDTO {

    @NotNull
    private Long id;

    @NotNull
    private StatePayment state;

    private String stripePaymentId;
}
