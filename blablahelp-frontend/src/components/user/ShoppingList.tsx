import {
    Button,
    Dialog,
    DialogActions,
    DialogContent,
    DialogContentText,
    DialogTitle,
    useMediaQuery,
    useTheme
} from "@mui/material";
import {useState} from "react";
import ShoppingListType from "../../types/ShoppingListType";
import Product from "./Product";

type ShoppingListProps = {
    data: ShoppingListType,
    mitshopperFirstname: string,
    shopperFirstname: string,
}

export default function ShoppingList(props: ShoppingListProps) {

    const [open, setOpen] = useState(false);
    const theme = useTheme();
    const fullScreen = useMediaQuery(theme.breakpoints.down('md'));

    const handleClickOpen = () => {
        setOpen(true);
    };

    const handleClose = () => {
        setOpen(false);
    };

    return (
        <>
            <Button onClick={handleClickOpen}
                    color={"primary"} variant={"text"} size={"small"}
                    sx={{display: "inline-block", paddingLeft: "0"}}>
                Shoppinglist</Button>
            <Dialog
                fullScreen={fullScreen}
                open={open}
                onClose={handleClose}
                aria-labelledby="Shoppinglist"
            >
                <DialogTitle id="Shoppinglist" variant={"h4"}>
                    {"Shoppinglist " + " von " + props.mitshopperFirstname + " für " + props.shopperFirstname}
                </DialogTitle>
                <DialogContent>
                    <DialogContentText component={"div"}>
                        {
                            (props.data.products && props.data.products.length > 0) &&
                            props.data.products.map((item, index) => {
                                return (<Product key={index} {...item}/>)
                            })
                        }

                    </DialogContentText>
                </DialogContent>
                <DialogActions>
                    <Button autoFocus onClick={handleClose}>Schließen</Button>

                </DialogActions>
            </Dialog>
        </>
    )

}
