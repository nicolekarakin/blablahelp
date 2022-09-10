import {ReviewType} from "./ReviewType";
import AddressType from "./AddressType";


type NotOwnOfferType = {
    offerId: string,

    shoppingDay: number,
    timeFrom: number,
    timeTo: number,

    shopname: string,
    shopAddress: AddressType,
    priceOffer: string, //TODO how to handle currency? for now assume euro,
    notes?: string,

    maxMitshoppers: number,
    maxLiter: number,
    maxArticles: number,
    maxDistanceKm: number,

    firstname: string,
    motto: string,

    shoppingCancellation: number,
    shoppingCount: number,
    shoppingRating: number,
    reviewsForShopping: ReviewType[],

}
export default NotOwnOfferType

