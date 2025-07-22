package kr.server.pointly.application.point;

import kr.server.pointly.domain.payment.Payment;
import kr.server.pointly.domain.payment.PaymentCommand;
import kr.server.pointly.domain.payment.PaymentService;
import kr.server.pointly.domain.payment.PGType;
import kr.server.pointly.domain.point.PointService;
import kr.server.pointly.domain.user.User;
import kr.server.pointly.domain.user.UserCommand;
import kr.server.pointly.domain.user.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class PointFacade {

    private final PointService pointService;
    private final UserService userService;
    private final PaymentService paymentService;

    public PointResult.ChargeRequest chargeRequest(PointCriteria.ChargeRequest criteria){
        User user = userService.find(new UserCommand.Find(criteria.userId()));
        Payment payment = paymentService.create(new PaymentCommand.Create(user, criteria.amount(), PGType.valueOf(criteria.paymentType())));
        return PointResult.ChargeRequest.from(user, payment);
    }
}
