import { useEffect, useState, useCallback } from 'react';
import ApiCliente from '../utils/ApiCliente';

export type UseFetchResult<T> = {
  data: T;
  error: string | null;
  loading: boolean;
  refetch: any;
};

const useFetch = <T>(url: string): UseFetchResult<T> => {
  const [data, setData] = useState<T>([] as T);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState<string | null>(null);

  const fetchData = useCallback(async () => {
    setLoading(true);
    try {
      const response = await ApiCliente.get(url);
      setData(response.data);
      setError(null);
    } catch (err: any) {
      setError(err.message || 'Error al cargar los datos');
    } finally {
      setLoading(false);
    }
  }, [url]);

  useEffect(() => {
    fetchData();
  }, [fetchData]);

  return { data, loading, error, refetch: fetchData };
};

export default useFetch;