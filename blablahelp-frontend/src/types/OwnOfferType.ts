import AddressType from "./AddressType";
import OwnInquiryType from "./OwnInquiryType";

type OwnOfferType = {
    offerId?: string,
    accountId: string,
    shoppingDay: number | undefined,
    timeFrom: number | undefined,
    timeTo: number | undefined,

    shopname: string | undefined,
    shopAddress: AddressType | null,
    destinationAddress: AddressType | null,

    maxMitshoppers: number | null,
    maxLiter: number | null,
    maxArticles: number | null,
    maxDistanceKm: number | null,

    isBooked?: boolean,
    isFullyBooked?: boolean,
    isReviewed?: boolean,
    isCanceled?: boolean,
    isExpired?: boolean,

    notes?: string,
    priceOffer: string | undefined, //TODO(@nicolekarakin) how to handle currency? for now assume euro,
    inquiries?: OwnInquiryType[],

}
export default OwnOfferType

