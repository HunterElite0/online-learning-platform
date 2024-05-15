/**
 * v0 by Vercel.
 * @see https://v0.dev/t/i142wjHcKiM
 * Documentation: https://v0.dev/docs#integrating-generated-code-into-your-nextjs-app
 */
import Link from "next/link";
import { AvatarImage, AvatarFallback, Avatar } from "@/components/ui/avatar";
import { Button } from "@/components/ui/button";
import {
  DropdownMenuTrigger,
  DropdownMenuLabel,
  DropdownMenuSeparator,
  DropdownMenuItem,
  DropdownMenuContent,
  DropdownMenu,
  DropdownMenuRadioItem,
  DropdownMenuRadioGroup,
} from "@/components/ui/dropdown-menu";
import { Input } from "@/components/ui/input";

export default function CoursesPage() {
  return (
    <main className="flex flex-col min-h-screen bg-gray-950 text-white">
      <div className="sticky top-0 container py-4 px-6 backdrop-blur-md bg-black/30">
        <header className="py-3 border-b-4">
          <div className="flex items-center justify-between">
            <Link className="flex items-center gap-2" href="#">
              <ArrowLeftIcon className="w-5 h-5" />
              <h1 className="text-2xl font-bold">Student Dashboard</h1>
            </Link>
            <DropdownMenu>
              <DropdownMenuTrigger asChild>
                <Button className="rounded-full" size="icon" variant="ghost">
                  <Avatar>
                    <AvatarImage alt="@shadcn" src="/placeholder-avatar.jpg" />
                    <AvatarFallback>JP</AvatarFallback>
                  </Avatar>
                  <span className="sr-only">Toggle user menu</span>
                </Button>
              </DropdownMenuTrigger>
              <DropdownMenuContent align="end">
                <DropdownMenuLabel>Jared Palmer</DropdownMenuLabel>
                <DropdownMenuSeparator />
                <DropdownMenuItem>Settings</DropdownMenuItem>
                <DropdownMenuItem>Logout</DropdownMenuItem>
              </DropdownMenuContent>
            </DropdownMenu>
          </div>
        </header>{" "}
        <div className="flex justify-between items-center mb-6 pt-3">
          <h2 className="text-2xl font-bold">All Courses</h2>
          <DropdownMenu>
            <DropdownMenuTrigger asChild>
              <Button className="flex items-center gap-2" variant="outline">
                <ListIcon className="w-4 h-4" />
                Sort by Rating
              </Button>
            </DropdownMenuTrigger>
            <DropdownMenuContent align="end">
              <DropdownMenuRadioGroup value="rating">
                <DropdownMenuRadioItem value="rating">
                  Rating
                </DropdownMenuRadioItem>
              </DropdownMenuRadioGroup>
            </DropdownMenuContent>
          </DropdownMenu>
        </div>
        <div className="flex justify-center mb-6">
          <form className="relative w-full max-w-md">
            <SearchIcon className="absolute left-3 top-1/2 -translate-y-1/2 w-5 h-5 text-gray-400" />
            <Input
              className="bg-gray-800 border-none pl-10 pr-4 py-2 rounded-md focus:ring-2 focus:ring-gray-400 focus:outline-none"
              placeholder="Search courses..."
              type="search"
            />
          </form>
        </div>
      </div>

      <section className="container mx-auto py-8 px-4 md:px-6 lg:px-8 ">
        <div className="grid grid-cols-1 sm:grid-cols-2 md:grid-cols-3 lg:grid-cols-4 gap-6" />
        <div className="grid grid-cols-1 sm:grid-cols-2 md:grid-cols-3 lg:grid-cols-4 gap-6">
          {/* <div className="bg-gray-800 shadow-md rounded-lg p-4 flex flex-col items-center justify-center gap-2 hover:bg-gray-700 transition-colors h-full">
            <img
              alt="Course Image"
              className="rounded-lg w-full h-40 object-cover"
              height={150}
              src="/placeholder.svg"
              style={{
                aspectRatio: "200/150",
                objectFit: "cover",
              }}
              width={200}
            />
            <h3 className="text-lg font-semibold line-clamp-2 text-center">Introduction to Web Development</h3>
            <div className="flex items-center gap-1 text-yellow-500">
              <StarIcon className="w-5 h-5" />
              <StarIcon className="w-5 h-5" />
              <StarIcon className="w-5 h-5" />
              <StarIcon className="w-5 h-5" />
              <StarIcon className="w-5 h-5 text-gray-600" />
              <span className="text-gray-400 text-sm">(4.5)</span>
            </div>
            <p className="text-gray-400 text-sm">$99.99</p>
            <Link className="w-full" href="#">
              <Button className="w-full" variant="outline">
                View Details
              </Button>
            </Link>
          </div> */}
          <div className="bg-gray-800 shadow-md rounded-lg p-4 flex flex-col items-center justify-center gap-2 hover:bg-gray-700 transition-colors h-full">
            <img
              alt="Course Image"
              className="rounded-lg w-full h-40 object-cover"
              height={150}
              src="/placeholder.svg"
              style={{
                aspectRatio: "200/150",
                objectFit: "cover",
              }}
              width={200}
            />
            <h3 className="text-lg font-semibold line-clamp-2 text-center">
              Advanced JavaScript Concepts
            </h3>
            <div className="flex items-center gap-1 text-yellow-500">
              <StarIcon className="w-5 h-5" />
              <StarIcon className="w-5 h-5" />
              <StarIcon className="w-5 h-5" />
              <StarIcon className="w-5 h-5" />
              <StarIcon className="w-5 h-5 text-gray-600" />
              <span className="text-gray-400 text-sm">(4.2)</span>
            </div>
            <p className="text-gray-400 text-sm">$149.99</p>
            <Link className="w-full" href="#">
              <Button className="w-full" variant="outline">
                View Details
              </Button>
            </Link>
          </div>
          <div className="bg-gray-800 shadow-md rounded-lg p-4 flex flex-col items-center justify-center gap-2 hover:bg-gray-700 transition-colors h-full">
            <img
              alt="Course Image"
              className="rounded-lg w-full h-40 object-cover"
              height={150}
              src="/placeholder.svg"
              style={{
                aspectRatio: "200/150",
                objectFit: "cover",
              }}
              width={200}
            />
            <h3 className="text-lg font-semibold line-clamp-2 text-center">
              React.js Fundamentals
            </h3>
            <div className="flex items-center gap-1 text-yellow-500">
              <StarIcon className="w-5 h-5" />
              <StarIcon className="w-5 h-5" />
              <StarIcon className="w-5 h-5" />
              <StarIcon className="w-5 h-5" />
              <StarIcon className="w-5 h-5" />
              <span className="text-gray-400 text-sm">(5.0)</span>
            </div>
            <p className="text-gray-400 text-sm">$199.99</p>
            <Link className="w-full" href="#">
              <Button className="w-full" variant="outline">
                View Details
              </Button>
            </Link>
          </div>
          <div className="bg-gray-800 shadow-md rounded-lg p-4 flex flex-col items-center justify-center gap-2 hover:bg-gray-700 transition-colors h-full">
            <img
              alt="Course Image"
              className="rounded-lg w-full h-40 object-cover"
              height={150}
              src="/placeholder.svg"
              style={{
                aspectRatio: "200/150",
                objectFit: "cover",
              }}
              width={200}
            />
            <h3 className="text-lg font-semibold line-clamp-2 text-center">
              Python for Data Science
            </h3>
            <div className="flex items-center gap-1 text-yellow-500">
              <StarIcon className="w-5 h-5" />
              <StarIcon className="w-5 h-5" />
              <StarIcon className="w-5 h-5" />
              <StarIcon className="w-5 h-5" />
              <StarIcon className="w-5 h-5 text-gray-600" />
              <span className="text-gray-400 text-sm">(4.3)</span>
            </div>
            <p className="text-gray-400 text-sm">$129.99</p>
            <Link className="w-full" href="#">
              <Button className="w-full" variant="outline">
                View Details
              </Button>
            </Link>
          </div>
          <div className="bg-gray-800 shadow-md rounded-lg p-4 flex flex-col items-center justify-center gap-2 hover:bg-gray-700 transition-colors h-full">
            <img
              alt="Course Image"
              className="rounded-lg w-full h-40 object-cover"
              height={150}
              src="/placeholder.svg"
              style={{
                aspectRatio: "200/150",
                objectFit: "cover",
              }}
              width={200}
            />
            <h3 className="text-lg font-semibold line-clamp-2 text-center">
              Machine Learning Fundamentals
            </h3>
            <div className="flex items-center gap-1 text-yellow-500">
              <StarIcon className="w-5 h-5" />
              <StarIcon className="w-5 h-5" />
              <StarIcon className="w-5 h-5" />
              <StarIcon className="w-5 h-5" />
              <StarIcon className="w-5 h-5 text-gray-600" />
              <span className="text-gray-400 text-sm">(4.4)</span>
            </div>
            <p className="text-gray-400 text-sm">$179.99</p>
            <Link className="w-full" href="#">
              <Button className="w-full" variant="outline">
                View Details
              </Button>
            </Link>
          </div>
          <div className="bg-gray-800 shadow-md rounded-lg p-4 flex flex-col items-center justify-center gap-2 hover:bg-gray-700 transition-colors h-full">
            <img
              alt="Course Image"
              className="rounded-lg w-full h-40 object-cover"
              height={150}
              src="/placeholder.svg"
              style={{
                aspectRatio: "200/150",
                objectFit: "cover",
              }}
              width={200}
            />
            <h3 className="text-lg font-semibold line-clamp-2 text-center">
              Ethical Hacking Essentials
            </h3>
            <div className="flex items-center gap-1 text-yellow-500">
              <StarIcon className="w-5 h-5" />
              <StarIcon className="w-5 h-5" />
              <StarIcon className="w-5 h-5" />
              <StarIcon className="w-5 h-5" />
              <StarIcon className="w-5 h-5 text-gray-600" />
              <span className="text-gray-400 text-sm">(4.6)</span>
            </div>
            <p className="text-gray-400 text-sm">$249.99</p>
            <Link className="w-full" href="#">
              <Button className="w-full" variant="outline">
                View Details
              </Button>
            </Link>
          </div>
        </div>
      </section>
    </main>
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

function ListIcon(props: any) {
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
      <line x1="8" x2="21" y1="6" y2="6" />
      <line x1="8" x2="21" y1="12" y2="12" />
      <line x1="8" x2="21" y1="18" y2="18" />
      <line x1="3" x2="3.01" y1="6" y2="6" />
      <line x1="3" x2="3.01" y1="12" y2="12" />
      <line x1="3" x2="3.01" y1="18" y2="18" />
    </svg>
  );
}

function SearchIcon(props: any) {
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
      <circle cx="11" cy="11" r="8" />
      <path d="m21 21-4.3-4.3" />
    </svg>
  );
}

function StarIcon(props: any) {
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
      <polygon points="12 2 15.09 8.26 22 9.27 17 14.14 18.18 21.02 12 17.77 5.82 21.02 7 14.14 2 9.27 8.91 8.26 12 2" />
    </svg>
  );
}
