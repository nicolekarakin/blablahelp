import OwnOfferType from "./OwnOfferType";
import {AddressWrapType} from "./AddressType";
import OwnInquiryType from "./OwnInquiryType";
export type UserDataType={
    usedAddresses:AddressWrapType[],
    currentOffers?:OwnOfferType[],
    currentInquiries?:OwnInquiryType[],
}
type UserType = {
    id: string,
    firstname: string,
    email: string,
    city: string,
    userData?:UserDataType,
}
export default UserType
