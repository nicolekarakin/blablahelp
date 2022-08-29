import AddressType from "./AddressType";

type OwnInquiryType = {
    id?: string,
    accountId: string,

    mitshopperAddress:AddressType |null,

    inquiryStatus:string|undefined,
    shoppingList?:{}|null,

    isDelivered?:boolean,
    isReviewed?:boolean,
    isCanceled?:boolean,
    isExpired?:boolean,

    notes?:string,
    inquiryPrice:string| undefined, //TODO how to handle currency? for now assume euro

}
export default OwnInquiryType