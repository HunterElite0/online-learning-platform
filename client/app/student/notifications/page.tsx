"use client";
import { useState, useEffect } from "react";


async function getAllNotifications(id : number) {
    const URL: string = "http://localhost:8080/learning/notifications/" + id;
    //const URL: string = "http://course-service/learning/notifications/"+ id;
    const res = await fetch(URL);
    const response = await res.json();
    console.log(response);
    return response;
  }

export default function Notifications() {

    const [notifications, setNotifications] = useState<any[]>([]);
    const cookies = require("js-cookie");

    useEffect(() => {
        const fetchNotification = async () => {
          const notifications = await getAllNotifications(cookies.get("id"));
          console.log(notifications);
          setNotifications(notifications);
        };
        fetchNotification();
      }, []);

    return (
      <div className="grid min-h-screen w-full place-items-center bg-gray-950 px-4 py-12 dark:bg-gray-950">
        <div className="w-full max-w-md space-y-6">
          <div className="space-y-2 text-center">
            <h1 className="text-3xl font-bold text-white">Notifications</h1>
          </div>
          <div className="space-y-4 rounded-lg border bg-gray-900 p-6 shadow-sm dark:border-gray-800 dark:bg-gray-900">
            
            <div className="flex items-start justify-between">
              <div className="flex items-start gap-3">
                <div className="flex h-8 w-8 items-center justify-center rounded-full bg-green-900/20 text-green-400">
                  <CheckIcon className="h-5 w-5" />
                </div>
                <div className="space-y-1">
                  <p className="font-medium text-white">
                    {notifications.length > 0 ? (
                        notifications.map((notification: any) => (
                            <div>
                            {notification.content}
                            </div>
                        ))
                    ) : (
                    <p className="text-center text-gray-400">No Notifications.</p>
                    )}
                  </p>
                </div>
              </div>
            </div>
          </div>
        </div>
      </div>
    )
  }
  
  function CheckIcon(props : any) {
    return (
      <svg
        {...props}
        xmlns="http://www.w3.org/2000/svg"
        width="24"
        height="24"
        viewBox="0 0 24 24"
        fill="none"
        stroke="currentColor"
        strokeWidth="2"
        strokeLinecap="round"
        strokeLinejoin="round"
      >
        <path d="M20 6 9 17l-5-5" />
      </svg>
    )
  }
  
  function XIcon(props : any) {
    return (
      <svg
        {...props}
        xmlns="http://www.w3.org/2000/svg"
        width="24"
        height="24"
        viewBox="0 0 24 24"
        fill="none"
        stroke="currentColor"
        strokeWidth="2"
        strokeLinecap="round"
        strokeLinejoin="round"
      >
        <path d="M18 6 6 18" />
        <path d="m6 6 12 12" />
      </svg>
    )
  }


  