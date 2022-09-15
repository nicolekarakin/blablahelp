import TextField from '@mui/material/TextField';
import {AdapterDateFns} from '@mui/x-date-pickers/AdapterDateFns';
import {DatePicker} from '@mui/x-date-pickers/DatePicker';
import React, {useEffect, useState} from 'react';
import {ClockPickerView, LocalizationProvider, MobileTimePicker} from "@mui/x-date-pickers";
import {addMinutes} from 'date-fns'

const shouldDisableMyTime=(timeValue:number, clockType:ClockPickerView) => {
    const mod=timeValue % 15
    return (clockType === 'minutes' && (!!mod));
}
type DateTimeOfferFormPartProps={
    shoppingDate:Date | null,
    setShoppingDate: React.Dispatch<React.SetStateAction<Date | null>>,
    timeTo:Date | null,
    setTimeTo: React.Dispatch<React.SetStateAction<Date | null>>,
    timeFrom:Date | null,
    setTimeFrom: React.Dispatch<React.SetStateAction<Date | null>>,
}
export default function DateTimeOfferFormPart(p:DateTimeOfferFormPartProps) {
    const [timeFromDisabled, setTimeFromDisabled] = useState<boolean>(true);
    const [timeToDisabled, setTimeToDisabled] = useState<boolean>(true);
    const [minTimeTo, setMinTimeTo] = useState<Date | null>(null);

    useEffect(() => {
        const dayOk = (p.shoppingDate && !isNaN(p.shoppingDate.getTime()))
        setTimeFromDisabled(!dayOk)
        const fromOk = (p.timeFrom && !isNaN(p.timeFrom.getTime()))
        setTimeToDisabled(!fromOk)
        if (!timeToDisabled) {
            const newMinDate:Date|undefined=addMinutes(p.timeFrom?p.timeFrom:0, 30)
            setMinTimeTo(newMinDate)
        }


    }, [p.shoppingDate, p.timeTo, p.timeFrom])

    const finalTime=(inputTime:Date|null)=>{
        const newMin=(inputTime?.getMinutes() || 0)+60*(inputTime?.getHours() || 0)
        return addMinutes(p.shoppingDate?p.shoppingDate:0,newMin)

    }
    const getYesterday=()=>{
        const yesterday=new Date();
        yesterday.setDate(yesterday.getDate() - 1)
        return yesterday
    }

    return (
        <LocalizationProvider dateAdapter={AdapterDateFns}>
            <DatePicker
                disablePast
                label="Shopping Tag"
                minDate={getYesterday()}
                value={p.shoppingDate}
                onChange={(newValue) => {
                    p.setShoppingDate(newValue);
                }}
                renderInput={(params) => <TextField
                    required
                    helperText="Tag: dd/mm/yyyy (z.B. 01/02/2022)"
                    {...params} />}
            />

            <MobileTimePicker
                disabled={timeFromDisabled}
                shouldDisableTime={shouldDisableMyTime}
                label="Geliefert ab"
                value={p.timeFrom}
                onChange={(newValue) => {
                    p.setTimeFrom(finalTime(newValue));
                }}
                renderInput={(params) => <TextField
                    required
                    helperText="Zeit: hh:mm am|pm (z.B. 01:59 pm)"
                    {...params} />}
            />

            <MobileTimePicker
                minTime={minTimeTo}
                disabled={timeToDisabled}
                shouldDisableTime={shouldDisableMyTime}
                label="Geliefert bis"
                value={p.timeTo}
                onChange={(newValue) => {
                    p.setTimeTo(finalTime(newValue));
                }}
                renderInput={(params) => <TextField
                    required
                    helperText="Zeit: hh:mm am|pm (z.B. 01:59 pm)"
                    {...params} />}
            />

        </LocalizationProvider>
    )
}


