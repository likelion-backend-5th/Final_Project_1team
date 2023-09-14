import { Button } from '@mui/material';
import { observer } from 'mobx-react';
import {
  Address,
  postcodeScriptUrl,
} from 'react-daum-postcode/lib/loadPostcode';
import useDaumPostcodePopup from 'react-daum-postcode/lib/useDaumPostcodePopup';
import userStore from '../../store/user/userStore';
import useStore from '../../store/useStores';

const AddressPost = () => {
  //클릭 시 수행될 팝업 생성 함수
  const userStore: userStore = useStore().userStore;
  const open = useDaumPostcodePopup(postcodeScriptUrl);

  const handleComplete = (data: Address) => {
    const zipcode = data.zonecode;
    const fullAddress = data.address;
    userStore.setAddress({ zipcode: zipcode, city: fullAddress });
  };
  const handleClick = () => {
    open({ onComplete: handleComplete });
  };
  return (
    <Button type="button" onClick={handleClick} fullWidth color="inherit">
      주소찾기
    </Button>
  );
};

export default observer(AddressPost);
