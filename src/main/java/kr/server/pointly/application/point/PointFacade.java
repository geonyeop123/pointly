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

    public PointResult.RequestCharge requestCharge(PointCriteria.RequestCharge criteria){
        User user = userService.find(new UserCommand.Find(criteria.userId()));
        Payment payment = paymentService.create(new PaymentCommand.Create(user, criteria.amount(), PGType.valueOf(criteria.paymentType())));
        return PointResult.RequestCharge.from(user, payment);
    }

    public PointResult.CancelCharge cancelCharge(PointCriteria.CancelCharge criteria) {
        User user = userService.find(new UserCommand.Find(criteria.userId()));
        Payment payment = paymentService.cancel(new PaymentCommand.Cancel(user, criteria.paymentId()));
        return PointResult.CancelCharge.from(user, payment);
    }
}
