import AddressType from "./AddressType";

type OwnOfferType = {
    offerId?: string,
    accountId: string,
    shoppingDay: number | undefined,
    timeFrom: number | undefined,
    timeTo: number | undefined,
    // city:string | undefined,

    shopname:string | undefined,
    shopAddress:AddressType | null,
    destinationAddress:AddressType |null,

    maxMitshoppers:number|null,
    maxLiter:number|null,
    maxArticles:number|null,
    maxDistanceKm:number|null,

    isBooked?:boolean,
    isFullyBooked?:boolean,
    isReviewed?:boolean,
    isCanceled?:boolean,
    isExpired?:boolean,

    notes?:string,
    priceOffer:string|undefined, //TODO how to handle currency? for now assume euro,
    inquiryIds?:string[],

}
export default OwnOfferType

