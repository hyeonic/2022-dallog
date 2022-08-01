import { useTheme } from '@emotion/react';
import { useMutation, useQueryClient } from 'react-query';
import { useRecoilValue } from 'recoil';

import { CategoryType } from '@/@types/category';

import { userState } from '@/recoil/atoms';

import Button from '@/components/@common/Button/Button';

import { CACHE_KEY, CONFIRM_MESSAGE } from '@/constants';

import subscriptionApi from '@/api/subscription';

import { categoryItem, item, unsubscribeButton } from './SubscribedCategoryItem.styles';

interface SubscribedCategoryItemProps {
  category: CategoryType;
  subscriptionId: number;
}

function SubscribedCategoryItem({ category, subscriptionId }: SubscribedCategoryItemProps) {
  const theme = useTheme();

  const { accessToken } = useRecoilValue(userState);

  const queryClient = useQueryClient();
  const { mutate } = useMutation(() => subscriptionApi.delete(accessToken, subscriptionId), {
    onSuccess: () => {
      queryClient.invalidateQueries(CACHE_KEY.SUBSCRIPTIONS);
    },
  });

  const handleClickUnsubscribeButton = () => {
    if (window.confirm(CONFIRM_MESSAGE.UNSUBSCRIBE)) {
      mutate();
    }
  };

  return (
    <div css={categoryItem}>
      <span css={item}>{category.createdAt.split('T')[0]}</span>
      <span css={item}>{category.name}</span>
      <div css={item}>
        <Button cssProp={unsubscribeButton(theme)} onClick={handleClickUnsubscribeButton}>
          구독중
        </Button>
      </div>
    </div>
  );
}

export default SubscribedCategoryItem;