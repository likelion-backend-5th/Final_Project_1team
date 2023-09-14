// eslint-disable-next-line @typescript-eslint/ban-ts-comment
// @ts-ignore
import {alertState} from '../../store/component/alertStore';
import useStore from '../../store/useStores';

export const useAlert = () => {
    const alertStore = useStore().alertStore;

    const openAlert = (alertState: alertState) => {
        alertStore.openAlert(alertState);
    };
    return {openAlert} as const;
};
