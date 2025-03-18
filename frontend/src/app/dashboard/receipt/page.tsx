"use client";

import { Button } from "@/components/ui/button";
import { Card, CardContent } from "@/components/ui/card";
import {
  Dropzone,
  DropZoneArea,
  DropzoneDescription,
  DropzoneFileList,
  DropzoneFileListItem,
  DropzoneFileMessage,
  DropzoneMessage,
  DropzoneRemoveFile,
  DropzoneRetryFile,
  DropzoneTrigger,
  InfiniteProgress,
  useDropzone,
} from "@/components/ui/dropzone";
import { useUserStore } from "@/stores/user-store";
import axios from "axios";
import { useCallback, useEffect, useState } from "react";
import { RefreshCw } from "lucide-react";

interface ReceiptResponse {
  image: string; // Base64 encoded image
  amount: number | null;
}

export default function Receipt() {
  const { id: userId, bearerToken } = useUserStore();
  const [isUploading, setIsUploading] = useState(false);
  const [receipts, setReceipts] = useState<ReceiptResponse[]>([]);
  const [isLoading, setIsLoading] = useState(false);

  const dropzone = useDropzone({
    onDropFile: async (file: File) => {
      try {
        setIsUploading(true);

        const formData = new FormData();
        formData.append("file", file);

        const response = await axios.post(
          `${process.env.NEXT_PUBLIC_API_URL}/api/${userId}/receipt`,
          formData,
          {
            headers: {
              Authorization: `Bearer ${bearerToken}`,
              "Content-Type": "multipart/form-data",
            },
          }
        );

        return {
          status: "success",
          result: URL.createObjectURL(file),
          serverData: response.data,
        };
      } catch (error) {
        console.error("Upload error:", error);
        return {
          status: "error",
          error: axios.isAxiosError(error)
            ? error.response?.data?.message || error.message
            : "Failed to upload receipt",
        };
      } finally {
        setIsUploading(false);
      }
    },
    validation: {
      accept: {
        "image/*": [".png", ".jpg", ".jpeg"],
      },
      maxSize: 10 * 1024 * 1024,
      maxFiles: 1,
    },
    shiftOnMaxFiles: true,
  });
  const fetchReceipts = useCallback(async () => {
    if (!userId) return;

    try {
      setIsLoading(true);
      const response = await axios.get<ReceiptResponse[]>(
        `http://localhost:8080/api/${userId}/receipt`,
        {
          headers: {
            Authorization: `Bearer ${bearerToken}`,
          },
        }
      );
      setReceipts(response.data);
    } catch (error) {
      console.error("Error fetching receipts:", error);
    } finally {
      setIsLoading(false);
    }
  }, [userId, bearerToken]);

  useEffect(() => {
    fetchReceipts();

    const intervalId = setInterval(() => {
      fetchReceipts();
    }, 60000);

    return () => clearInterval(intervalId);
  }, [fetchReceipts]);

  useEffect(() => {
    if (isUploading === false) {
      fetchReceipts();
    }
  }, [isUploading, fetchReceipts]);

  return (
    <div>
      <div className="flex justify-between items-center mb-4">
        <h2 className="text-xl font-bold">Receipts</h2>
        <Button
          variant="outline"
          size="sm"
          onClick={() => fetchReceipts()}
          disabled={isLoading}
        >
          {isLoading ? (
            <>
              <RefreshCw className="mr-2 h-4 w-4 animate-spin" />
              Refreshing...
            </>
          ) : (
            <>
              <RefreshCw className="mr-2 h-4 w-4" />
              Refresh
            </>
          )}
        </Button>
      </div>
      <Dropzone {...dropzone}>
        <div className="flex justify-between">
          <DropzoneMessage />
        </div>
        <DropZoneArea>
          <DropzoneTrigger className="flex gap-8 bg-transparent text-sm">
            <div className="flex flex-col gap-1 font-semibold">
              <p>Upload Receipt</p>
            </div>
          </DropzoneTrigger>
        </DropZoneArea>
      </Dropzone>
      <div className="grid grid-cols-3 gap-4 p-4">
        {receipts.map((receipt, index) => (
          <Card key={index} className="w-64">
            <CardContent className="p-4 flex flex-col items-center">
              <img
                src={`data:image/png;base64,${receipt.image}`}
                alt={`Receipt ${index}`}
                className="w-full h-40 object-cover rounded-lg"
              />
              <p className="text-center font-semibold mt-2">
                {receipt.amount !== null
                  ? `$${receipt.amount.toFixed(2)}`
                  : "Unknown"}
              </p>
            </CardContent>
          </Card>
        ))}
      </div>
    </div>
  );
}
