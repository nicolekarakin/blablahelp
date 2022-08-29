import AddressType from "./AddressType";

type OwnOfferType = {
    id?: string,
    accountId: string,
    date: number | undefined,
    timeFrom: number | undefined,
    timeTo: number | undefined,
    city:string | undefined,

    shopname:string | undefined,
    shopAddress:AddressType | null,
    destinationAddress:AddressType |null,

    maxMitshoppers:number|null,
    maxDrinks:number|null,
    maxArticles:number|null,
    maxDistanceKm:number|null,

    isVisible?:boolean,
    isReviewed?:boolean,
    isCanceled?:boolean,
    isExpired?:boolean,

    notes?:string,
    priceOffer:string| undefined, //TODO how to handle currency? for now assume euro

}
export default OwnOfferType
