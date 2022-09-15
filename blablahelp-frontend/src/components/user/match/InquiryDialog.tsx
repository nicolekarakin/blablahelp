import * as React from 'react';
import {useState} from 'react';
import Button from '@mui/material/Button';
import Dialog from '@mui/material/Dialog';
import DialogActions from '@mui/material/DialogActions';
import DialogContent from '@mui/material/DialogContent';
import DialogContentText from '@mui/material/DialogContentText';
import DialogTitle from '@mui/material/DialogTitle';
import DynamicTextInput from "../DynamicTextInput";

type InquiryDialogProp = {
    handleSubmit: (shoplistTitle: string) => void,
}

export default function InquiryDialog(props: InquiryDialogProp) {
    const [open, setOpen] = useState(false);
    const [shoplistTitle, setShoplistTitle] = useState()

    const handleClickOpen = () => {
        setOpen(true);
    };

    const handleClose = () => {
        setOpen(false);
    };

    const handleSubmit = () => {
        props.handleSubmit(shoplistTitle!)
        setOpen(false)
    };

    const getShoplistTitles = () => {
        const lists = [{title: "Grundnahrungsmittel", item: "Grundnahrungsmittel"},
            {title: "Gesundesessen", item: "Gesundesessen"},
            {title: "Lieblingsessen", item: "Lieblingsessen"}]
        return Promise.resolve([...lists]);
    }

    return (
        <div>
            <Button autoFocus onClick={handleClickOpen}>Mitshopping Bestätigen</Button>

            <Dialog open={open} onClose={handleClose}>
                <DialogTitle>Mitshopping Bestätigen</DialogTitle>
                <DialogContent>
                    <DialogContentText>
                        To subscribe to this website, please enter your email address here. We
                        will send updates occasionally.
                    </DialogContentText>

                    <DynamicTextInput
                        label={"Shoplist Title"}
                        required={true}
                        disabled={false}
                        getData={getShoplistTitles}
                        setSelectValue={setShoplistTitle}
                        selectValue={shoplistTitle}
                        helperText=""
                        axiosProps={{}}
                        noOptionsText={"Leider kein Shoplist mit diesen Title vorhanden"}
                    />
                </DialogContent>
                <DialogActions>
                    <Button onClick={handleClose}>Schließen</Button>
                    <Button variant={"contained"} onClick={handleSubmit}>Abschicken</Button>
                </DialogActions>
            </Dialog>
        </div>
    );
}
