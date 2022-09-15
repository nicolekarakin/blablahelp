export type ProductType = {
    "title": string,
    "category": string[],
    "amount": number,
    "unit": string,
    "isBought"?: boolean
}

type ShoppingListType = {
    "title": string,
    "accountId": string,
    "products": ProductType[]
}

export default ShoppingListType