
import React from 'react'
import { Card, CardHeader, CardContent } from '../../../components/ui/card'
import { Button } from '../../../components/ui/button'

const groups = [
    {
        name: 'Family',
        members: ['Alice', 'Bob', 'Charlie']
    },
    {
        name: 'Friends',
        members: ['David', 'Eva', 'Frank']
    },
    {
        name: 'Colleagues',
        members: ['Grace', 'Heidi']
    }
]

const GroupsPage = () => {
    return (
        <div className="p-8">
            <div className="flex justify-between items-center mb-6">
                <h1 className="text-2xl font-bold">Groups</h1>
                <Button onClick={() => { /* future implementation */ }}>
                    Add Group
                </Button>
            </div>

            {/* Grid of group cards */}
            <div className="grid gap-6 grid-cols-1 sm:grid-cols-2 lg:grid-cols-3">
                {groups.map((group, index) => (
                    <Card key={index} className="p-4 shadow">
                        <CardHeader>
                            <h2 className="text-xl font-semibold">{group.name}</h2>
                        </CardHeader>
                        <CardContent>
                            <ul className="list-disc pl-5">
                                {group.members.map((member, idx) => (
                                    <li key={idx}>{member}</li>
                                ))}
                            </ul>
                        </CardContent>
                    </Card>
                ))}
            </div>
        </div>
    )
}

export default GroupsPage
