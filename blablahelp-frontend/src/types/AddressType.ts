import LocType from "./LocType";

type AddressType = {
    city: string,
    country: string,
    street: string,
    zip:string,
    loc?:LocType,
}
export default AddressType
