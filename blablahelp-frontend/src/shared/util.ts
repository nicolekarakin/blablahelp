import AddressType from "../types/AddressType";

export const cancellationDays=2

export const capitalise = (str: string) => {
    return str.replace(/^./, str[0].toUpperCase());
}
export const dateFromInstant=(instant:number,locale:string)=>{
    const date = new Date(instant);// * 1000
    const options:  Intl.DateTimeFormatOptions = { weekday: 'long', year: 'numeric', month: 'long', day: 'numeric' };
    return date.toLocaleDateString((locale)?locale:undefined, options)
}

export const dateFromInstantMinusDays=(instant:number,locale:string, days:number)=>{
    const date = new Date(instant);// * 1000
    date.setDate(date.getDate() - days);
    return dateTimeFromInstant(date.getTime(),locale)
}

export const dateTimeFromInstant=(instant:number,locale:string)=>{
    const date = new Date(instant);// * 1000
    return new Intl.DateTimeFormat(locale, { dateStyle: 'full', timeStyle: 'short' }).format(date);
}

export const timeFromInstant=(instant:number,locale:string)=>{
    const date = new Date(instant);// * 1000
    // return new Intl.RelativeTimeFormat(locale, {  }).format(date);
    return new Intl.DateTimeFormat(locale, { hour: 'numeric',minute:"numeric" }).format(date);
}

export const addressToString=(address:AddressType)=>{
    return address.street+", "+address.zip+" "+address.city;
}

export const getShortMonthString=(instant:number)=>{
    return new Date(instant).toLocaleString('default', { month: 'short' });
}
