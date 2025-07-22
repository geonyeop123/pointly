package kr.server.pointly.application.point;

import kr.server.pointly.domain.payment.PGType;
import kr.server.pointly.domain.payment.Payment;
import kr.server.pointly.domain.payment.PaymentCommand;
import kr.server.pointly.domain.payment.PaymentService;
import kr.server.pointly.domain.payment.client.PaymentClientRouter;
import kr.server.pointly.domain.point.Point;
import kr.server.pointly.domain.point.PointCommand;
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
    private final PaymentClientRouter paymentClientRouter;

    public PointResult.RequestCharge requestCharge(PointCriteria.RequestCharge criteria){
        User user = userService.find(new UserCommand.Find(criteria.userId()));
        Payment payment = paymentService.create(new PaymentCommand.Create(user, criteria.amount(), criteria.orderId(), PGType.valueOf(criteria.paymentType())));
        return PointResult.RequestCharge.from(user, payment);
    }

    public PointResult.CancelCharge cancelCharge(PointCriteria.CancelCharge criteria) {
        User user = userService.find(new UserCommand.Find(criteria.userId()));
        Payment payment = paymentService.cancel(new PaymentCommand.Cancel(user, criteria.paymentId()));
        return PointResult.CancelCharge.from(user, payment);
    }

    public PointResult.CompleteCharge completeCharge(PointCriteria.CompleteCharge criteria) {
        User user = userService.find(new UserCommand.Find(criteria.userId()));
        PaymentCommand.Complete command = new PaymentCommand.Complete(user, criteria.paymentId(), criteria.amount(), criteria.orderId(), PGType.valueOf(criteria.pgType()), criteria.paymentToken());
        Payment payment = paymentService.complete(command);
        Point point = pointService.charge(new PointCommand.Charge(user.getId(), criteria.amount()));
        try {
            paymentClientRouter.getClient(command.PGType()).completeRequest(command);
        } catch(Exception e){
            paymentService.fail(new PaymentCommand.Fail(user, payment.getId()));
            throw e;
        }

        return PointResult.CompleteCharge.from(user, payment, point);
    }
}
