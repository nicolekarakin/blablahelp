export const capitalise = (str: string) => {
    return str.replace(/^./, str[0].toUpperCase());
}
export const dateFromInstant=(instant:number)=>{
    const date = new Date(instant * 1000);
    const options:  Intl.DateTimeFormatOptions = { weekday: 'long', year: 'numeric', month: 'long', day: 'numeric' };
    return date.toLocaleDateString(undefined, options)
}

export const dateTimeFromInstant=(instant:number,locale:string)=>{
    const date = new Date(instant * 1000);
    return new Intl.DateTimeFormat(locale, { dateStyle: 'full', timeStyle: 'short' }).format(date);
}
