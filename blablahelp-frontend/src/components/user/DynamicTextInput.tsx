import {useEffect, useState,Fragment} from "react";
import {useSnackbar} from "notistack";

import {Autocomplete, CircularProgress, TextField} from "@mui/material";

type T={title:string,item:any}
type DynamicTextInputProps={
    axiosProps:{},
    getData:(axiosProps:{} | any)=> Promise<any[]>,
    label:string,
    required:boolean,
    disabled:boolean,
    helperText:string,
    noOptionsText:string,
    setSelectValue: React.Dispatch<React.SetStateAction<any | null>>,
    selectValue:any|null,
}
export default function DynamicTextInput(props:DynamicTextInputProps){

    const [options,setOptions]= useState<readonly T[]>([])
    const [open, setOpen] = useState(false);
    const [errorMessage, setErrorMessage] = useState("");
    const loading = open && options.length === 0 && (errorMessage.length===0);

    const {enqueueSnackbar} = useSnackbar();

    useEffect(() => {
        let active = true;
        if (!loading)return undefined;
        (async () => {
                if (active) {
                    props.getData(props.axiosProps)
                        .then(data => {
                            if(data.length===0){
                                setErrorMessage('Sorry, we don\'t have any data entries for your query')
                            }
                            setOptions([...data])
                        })
                        .catch(_ => {
                            enqueueSnackbar('Failed to load data', {variant: "error"})
                        });
                }
            }
        )();
        return () => {
            active = false
        }
    }, [loading]);


    useEffect(() => {
        if (!open) {
            setOptions([]);
        }
    }, [open]);

    const handleChange = (event: any, newValue: T | null) => {
        props.setSelectValue(newValue?.item);
    }

    return(
        <Autocomplete
            onChange={handleChange}
            open={open}
            onOpen={() => {
                setOpen(true);
            }}
            onClose={() => {
                setOpen(false);
            }}
            noOptionsText={props.noOptionsText}
            isOptionEqualToValue={(option, value) => option.title === value.title}
            getOptionLabel={(option) => option.title}
            options={options}
            loading={loading}
            disabled={props.disabled}
            renderInput={(params) => (
                <TextField
                    {...params}
                    label={props.label}
                    required={props.required}
                    helperText={props.helperText}
                    InputProps={{
                        ...params.InputProps,
                        endAdornment: (
                            <Fragment>
                                {loading ? <CircularProgress color="inherit" size={20} /> : null}
                                {params.InputProps.endAdornment}
                            </Fragment>
                        ),
                    }}
                />
            )}
        />
    )
}
