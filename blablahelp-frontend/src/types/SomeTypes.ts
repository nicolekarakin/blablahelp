import {OptionsObject, SnackbarMessage,SnackbarKey} from "notistack";

export type enqueueSnackbarType=(message: SnackbarMessage, options?: (OptionsObject | undefined)) => SnackbarKey
