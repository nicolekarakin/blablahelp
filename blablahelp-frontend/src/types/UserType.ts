import OwnOfferType from "./OwnOfferType";
import {AddressWrapType} from "./AddressType";
import OwnInquiryType from "./OwnInquiryType";
import {ReviewType} from "./ReviewType";

export type UserDataType={
    accountId:string,
    version?:number,
    motto?:string,

    shoppingCancellation?:number,
    shoppingCount?:number,
    shoppingRating?:number,

    mitShoppingCancellation?: number,
    mitShoppingCount?: number,
    mitShoppingRating?: number,

    usedAddresses: AddressWrapType[],

    reviewsForMitshopping?: ReviewType[],
    reviewsForShopping?: ReviewType[],
}
export type UserType = {
    id: string,
    firstname: string,
    email: string,
    city: string,
    userData?: UserDataType,
    currentOffers?: OwnOfferType[],
    currentInquiries?: OwnInquiryType[],
}
export default UserType
