"use client";
import Link from "next/link";
import { useState, useEffect } from "react";

async function getAllNotifications(token: string) {
  const URL: string = "http://localhost:8080/learning/notifications/";
  //const URL: string = "http://course-service/learning/notifications/";
  const res = await fetch(URL, {
    method: "GET",
    headers: {
      "Content-Type": "application/json",
      jwt: token,
    },
  });
  const response = await res.json();
  return response;
}

export default function Notifications() {
  const [notifications, setNotifications] = useState<any[]>([]);
  const cookies = require("js-cookie");

  useEffect(() => {
    const fetchNotification = async () => {
      const notifications = await getAllNotifications(cookies.get("jwt"));
      // console.log(notifications);
      setNotifications(notifications);
    };
    fetchNotification();
  }, []);

  return (
    <div className="grid min-h-screen w-full place-items-center bg-gray-950 px-4 py-12 dark:bg-gray-950">
      <div className="w-full max-w-md space-y-6">
        <Link className="flex items-center gap-2" href="/student">
          <ArrowLeftIcon className="w-5 h-5" />
          <h1 className="text-3xl font-bold">Student Dashboard</h1>
        </Link>
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
                {notifications.length > 0 ? (
                  notifications.map((notification: any) => (
                    <h3 key={notification.id}>{notification.content}</h3>
                  ))
                ) : (
                  <p className="text-center text-gray-400">No Notifications.</p>
                )}
              </div>
            </div>
          </div>
        </div>
      </div>
    </div>
  );
}

function CheckIcon(props: any) {
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
  );
}

function XIcon(props: any) {
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
  );
}

function ArrowLeftIcon(props: any) {
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
      <path d="m12 19-7-7 7-7" />
      <path d="M19 12H5" />
    </svg>
  );
}
