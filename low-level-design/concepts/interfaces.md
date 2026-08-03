# Interfaces — Ports, Not Ceremony

## When to introduce an interface

1. **Two+ implementations exist or are imminent** (Stripe/Adyen, SMS/email).  
2. **Tests need a fake** (clock, gateway, motor).  
3. **Independent deploy/vendor** boundary.

## When not to

- Single stable concrete with no test seam needed yet  
- Interface identical to one class 1:1 with no abstraction benefit  

## Good port examples

`PaymentGateway`, `NotificationChannel`, `Clock`, `SpotAllocator`, `DispatchStrategy`

## Bad

`IParkingLotServiceImpl` renaming, or leaking `StripeChargeRequest` through the port.

## Java 25 tip

Prefer small interfaces; sealed interfaces when the set of implementations should be closed and exhaustive in switches.
