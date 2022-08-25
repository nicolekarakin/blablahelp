import OwnOfferType from "./OwnOfferType";

type UserType = {
    id: string,
    firstname: string,
    email: string,
    city: string,
    ownOffers?:OwnOfferType[],
}
export default UserType
