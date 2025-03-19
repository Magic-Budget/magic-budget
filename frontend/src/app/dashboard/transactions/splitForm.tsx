import { MultiSelect, Option as MultiSelectOption } from '@/components/ui/multiselect';
import axios from 'axios';
import React from 'react';
import { useUserStore } from '@/stores/user-store';


const SplitForm: React.FC = () => {
    const userId = useUserStore((state) => state.id);
    const apiURL = `${process.env.NEXT_PUBLIC_API_URL}/api/${userId}`
    
    const getFriends = () => {
        let friends: MultiSelectOption[] = [];
        axios.get(`${apiURL}/friends`)
        .then((response) => {
            const friendsData = response.data;
            friends = friendsData.map((friend: any) => {
                return {
                    label: friend.name,
                    value: friend.id
                }
            });
        })
        .catch((error) => {friends=[]});
    
        return friends;
    }

    const friends = getFriends();

    return (
        <div>
            <MultiSelect
                className='mt-2'
                options={[
                    { label: 'Person 1', value: '1' },
                    { label: 'Person 2', value: '2' },
                    { label: 'Person 3', value: '3' },
                ]}
                placeholder="Select people to split with...">
                </MultiSelect>
        </div>
    );
};

export default SplitForm;