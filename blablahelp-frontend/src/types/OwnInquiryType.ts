import AddressType from "./AddressType";
import ShoppingListType from "./ShoppingListType";
import NotOwnOfferType from "./NotOwnOfferType";

export type OwnInquiryResponseType = {
    offer: NotOwnOfferType,
    inquiry: OwnInquiryType,
}

type OwnInquiryType = {
    offerId: string,
    mitshopperAccountId: string,
    mitshopperFirstname: string,
    mitshopperAddress: AddressType | null,
    inquiryPrice?: string, //TODO how to handle currency? for now assume euro
    notes?: string,
    shoppingList: ShoppingListType,

    inquiryStatus?: string,
    isDelivered?: boolean,
    isReviewed?: boolean,
    isCanceled?: boolean,
    isExpired?: boolean,
}
export default OwnInquiryType
