import { alertState } from '../../store/component/AlertStore';
import useStore from '../../store/useStores';

export const useAlert = () => {
  const alertStore = useStore().alertStore;

  const openAlert = (alertState: alertState) => {
    alertStore.openAlert(alertState);
  };
  return { openAlert } as const;
};
