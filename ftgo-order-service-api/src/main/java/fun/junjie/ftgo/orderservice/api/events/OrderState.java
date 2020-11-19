package fun.junjie.ftgo.orderservice.api.events;

public enum OrderState {
    APPROVAL_PENDING,
    APPROVED,
    REJECTED,
    CANCEL_PENDING,
    CANCELED,
    REVISION_PENDING
}
