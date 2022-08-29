import OwnOfferType from "./OwnOfferType";
import AddressType from "./AddressType";
import OwnInquiryType from "./OwnInquiryType";
type UserDataType={
    usedAddresses:AddressType[],
    currentOffers:OwnOfferType[],
    currentInquiries:OwnInquiryType[],
}
type UserType = {
    id: string,
    firstname: string,
    email: string,
    city: string,
    userData?:UserDataType,
}
export default UserType
