import { AxiosError, AxiosResponse } from 'axios';
import { useRef, useState } from 'react';
import { useMutation, useQuery, useQueryClient } from 'react-query';
import { useNavigate } from 'react-router-dom';
import { useRecoilValue } from 'recoil';

import { ProfileType } from '@/@types/profile';

import { userState } from '@/recoil/atoms';

import Button from '@/components/@common/Button/Button';
import Fieldset from '@/components/@common/Fieldset/Fieldset';

import { CACHE_KEY, CONFIRM_MESSAGE, PATH } from '@/constants';

import { clearAccessToken, createPostBody } from '@/utils';

import profileApi from '@/api/profile';

import { AiOutlineCheck } from 'react-icons/ai';
import { FiEdit3 } from 'react-icons/fi';

import {
  contentStyle,
  emailStyle,
  imageStyle,
  inputStyle,
  layoutStyle,
  logoutButtonStyle,
  menu,
  menuTitle,
  nameButtonStyle,
  nameStyle,
} from './Profile.styles';

function Profile() {
  const [isEditingName, setEditingName] = useState(false);

  const inputRef = {
    displayName: useRef<HTMLInputElement>(null),
  };

  const navigate = useNavigate();

  const { accessToken } = useRecoilValue(userState);

  const queryClient = useQueryClient();
  const { error, data } = useQuery<AxiosResponse<ProfileType>, AxiosError>(CACHE_KEY.PROFILE, () =>
    profileApi.get(accessToken)
  );

  const { mutate } = useMutation(
    (body: { displayName: string }) => profileApi.patch(accessToken, body),
    {
      onSuccess: () => {
        queryClient.invalidateQueries(CACHE_KEY.PROFILE);
      },
    }
  );

  if (error) {
    return <div>Error</div>;
  }

  const handleClickModifyButton = () => {
    setEditingName(true);
  };

  const handleClickCompleteButton = (defaultName: string | undefined) => {
    if (defaultName === undefined) {
      return;
    }

    const body = createPostBody(inputRef);

    if (body.displayName === '') {
      body.displayName = defaultName;
    }

    mutate(body);
    setEditingName(false);
  };

  const handleClickLogoutButton = () => {
    if (window.confirm(CONFIRM_MESSAGE.LOGOUT)) {
      clearAccessToken();
      navigate(PATH.MAIN);
      location.reload();
    }
  };

  return (
    <div css={layoutStyle}>
      <img src={data?.data.profileImageUrl} css={imageStyle} alt="프로필 이미지" />
      <div css={contentStyle}>
        {isEditingName ? (
          <form css={nameButtonStyle}>
            <Fieldset
              defaultValue={data?.data.displayName}
              placeholder={data?.data.displayName}
              refProp={inputRef.displayName}
              cssProp={inputStyle}
              autoFocus={true}
            />
            <Button
              type="submit"
              cssProp={menu}
              onClick={() => handleClickCompleteButton(data?.data.displayName)}
            >
              <AiOutlineCheck size={14} />
              <span css={menuTitle}>완료</span>
            </Button>
          </form>
        ) : (
          <div>
            <span css={nameStyle}>{data?.data.displayName}</span>
            <Button cssProp={menu} onClick={handleClickModifyButton}>
              <FiEdit3 size={14} />
              <span css={menuTitle}>수정</span>
            </Button>
          </div>
        )}
        <span css={emailStyle}>{data?.data.email}</span>
      </div>
      <Button cssProp={logoutButtonStyle} onClick={handleClickLogoutButton}>
        로그아웃
      </Button>
    </div>
  );
}

export default Profile;